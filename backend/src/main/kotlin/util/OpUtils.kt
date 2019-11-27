package util

import call.*
import exposed.dao.MmSolutionEvent
import exposed.dao.MmUser
import exposed.dao.toBaseFixed
import exposed.dsl.MmSolutionEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import optaplanner.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.optaplanner.core.api.solver.Solver
import java.util.*

fun BaseFlexibleEvent.toPlanning(): PlanningFlexibleEvent {
    return PlanningFlexibleEvent(name, type, longitude, latitude, duration)
}

fun PlanningFlexibleEvent.toPlannedFixed(): PlannedFixedEvent {
    return PlannedFixedEvent(name, longitude, latitude, startTime, startTime + duration)
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
        if (it.isOpPlanned) planned += it.toBaseFixed()
        else fixed += it.toBaseFixed()
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
        solver: Solver<EventSchedule>,
        mmUser: MmUser,
        onOpPidCreated: (MmSolveStatus) -> Unit
): MmSolveStatus {
    val pid = UUID.randomUUID().toString()
    onOpPidCreated(MmSolveStatus(pid, false))

    // solve
    val unsolvedEventSchedule = EventSchedule(
            fixedEvents, toPlanEvents.map { it.toPlanning() }, userPreferences
    )
    val solvedEventSchedule = withContext(Dispatchers.Default) {
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
    return MmSolveStatus(pid, true)
}