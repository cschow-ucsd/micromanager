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
import java.lang.IllegalStateException
import java.util.*

fun BaseFlexibleEvent.toPlanning(): PlanningFlexibleEvent {
    return PlanningFlexibleEvent(name, type, duration, longitude, latitude)
}

fun PlanningFlexibleEvent.toPlannedFixed(): PlannedFixedEvent {
    return PlannedFixedEvent(name, startTime, startTime + duration, longitude, latitude)
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
): List<MmSolveStatus> = transaction {
    MmSolutionEvent.find {
        (MmSolutionEvents.mmUserId eq mmUser.id) and (MmSolutionEvents.opPID inList opPIDs)
    }.distinctBy { it.opPID }.map {
        MmSolveStatus(it.opPID, true)
    }
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
            fixedEvents, toPlanEvents.map { it.toPlanning() }, userPreferences, currentTime
    )
    val solvedEventSchedule = withContext(Dispatchers.Default) {
        solver.solve(unsolvedEventSchedule)
    }
    transaction {
        MmSolutionEvents.batchInsert(
                solvedEventSchedule.let { it.planningFlexibleEventList + it.userFixedEventList }
        ) {
            this[MmSolutionEvents.opPID] = pid
            this[MmSolutionEvents.mmUserId] = mmUser.id

            // event details
            this[MmSolutionEvents.name] = it.name
            this[MmSolutionEvents.longitude] = it.longitude
            this[MmSolutionEvents.latitude] = it.latitude
            when (it) {
                is PlanningFlexibleEvent -> {
                    this[MmSolutionEvents.isOpPlanned] = true
                    this[MmSolutionEvents.startTime] = it.startTime
                    this[MmSolutionEvents.endTime] = it.startTime + it.duration
                }
                is BaseFixedEvent -> {
                    this[MmSolutionEvents.isOpPlanned] = false
                    this[MmSolutionEvents.startTime] = it.startTime
                    this[MmSolutionEvents.endTime] = it.endTime
                }
                else -> throw IllegalStateException("Unsupported event type!")
            }
        }
    }
    return MmSolveStatus(pid, true)
}