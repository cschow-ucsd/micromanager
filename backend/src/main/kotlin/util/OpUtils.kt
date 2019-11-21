package util

import call.*
import exposed.dao.MmSolutionEvent
import exposed.dao.MmUser
import exposed.dsl.MmSolutionEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import op.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.optaplanner.core.api.solver.SolverFactory
import java.util.*

fun BaseFlexibleEvent.toPlanning(): PlanningFlexibleEvent {
    return PlanningFlexibleEvent(name, type, longitude, latitude, duration)
}

fun PlanningFlexibleEvent.toFixed(): PlannedFixedEvent {
    val endTime = startTime + duration
    return PlannedFixedEvent(name, longitude, latitude, startTime, endTime)
}

fun MmSolutionEvent.Companion.findSolution(
        mmUser: MmUser,
        opPID: OpPID
): MmSolutionResponse {
    val fixed = mutableListOf<BaseFixedEvent>()
    val planned = mutableListOf<BaseFixedEvent>()
    find {
        (MmSolutionEvents.mmUserId eq mmUser.id) and (MmSolutionEvents.opPID eq opPID)
    }.forEach {
        if (it.isOpPlanned) planned.add(it)
        else fixed.add(it)
    }
    return MmSolutionResponse(fixed, planned)
}

fun MmSolutionEvent.Companion.getSolutionStatuses(
        mmUser: MmUser,
        opPIDs: OpPIDs
): List<MmSolveStatus> = MmSolutionEvent.find {
    (MmSolutionEvents.mmUserId eq mmUser.id) and (MmSolutionEvents.opPID inList opPIDs)
}.distinctBy { it.opPID }.map {
    MmSolveStatus(it.opPID, true)
}

suspend inline fun MmProblemRequest.solve(
        mmUser: MmUser,
        onOpPidCreated: (OpPID) -> Unit
): OpPID {
    val pid = UUID.randomUUID().toString()
    onOpPidCreated(pid)

    // start solving
    val solverFactory: SolverFactory<EventSchedule> = SolverFactory
            .createFromXmlResource<EventSchedule>("event_schedule_solver_configuration.xml")
    val solver = solverFactory.buildSolver()
    // TODO: Pass in problem to solve
    val unsolvedEventSchedule = EventSchedule()
    val solvedEventSchedule = withContext(Dispatchers.IO) {
        solver.solve(unsolvedEventSchedule)
    }
    transaction {
        MmSolutionEvents.batchInsert(solvedEventSchedule.planningFlexibleEventList) {
            this[MmSolutionEvents.opPID] = pid
            this[MmSolutionEvents.isOpPlanned] = true
            this[MmSolutionEvents.mmUserId] = mmUser.id

            // event details
            this[MmSolutionEvents.name] = it.name
            this[MmSolutionEvents.startTime] = it.startTime
            this[MmSolutionEvents.endTime] = it.startTime + it.duration
            this[MmSolutionEvents.longitude] = it.longitude
            this[MmSolutionEvents.latitude] = it.latitude
        }
    }
    return pid
}