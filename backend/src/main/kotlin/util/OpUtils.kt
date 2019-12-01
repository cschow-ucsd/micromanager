package util

import api.TravelTimeRequest
import call.MmProblemRequest
import call.MmSolutionResponse
import call.MmSolveStatus
import exposed.dao.MmSolutionEvent
import exposed.dao.MmSolutionSchedule
import exposed.dao.MmUser
import exposed.dao.toBaseFixed
import exposed.dsl.MmSolutionEvents
import exposed.dsl.MmSolutionSchedules
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import optaplanner.*
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction
import org.optaplanner.core.api.solver.Solver
import java.util.*

fun BaseFlexibleEvent.toPlanning(): PlanningFlexibleEvent {
    return PlanningFlexibleEvent(name, type, duration, longitude, latitude)
}

fun PlanningFlexibleEvent.toPlannedFixed(): PlannedFixedEvent {
    return PlannedFixedEvent(name, startTime, startTime + duration, longitude, latitude)
}

fun MmSolutionSchedule.Companion.findSolution(
        mmUser: MmUser,
        opPID: OpPID
): MmSolutionResponse {
    val fixed = mutableListOf<BaseFixedEvent>()
    val planned = mutableListOf<BaseFixedEvent>()
    val schedule = transaction { MmSolutionSchedule[opPID.value] }
    if (schedule.mmUser.id != mmUser.id) throw RuntimeException("User does not have access to this schedule.")
    transaction {
        schedule.events.forEach {
            if (it.isOpPlanned) planned += it.toBaseFixed()
            else fixed += it.toBaseFixed()
        }
    }
    return MmSolutionResponse(fixed, planned)
}


fun MmSolutionSchedule.Companion.getSolutionStatuses(
        mmUser: MmUser,
        opPIDs: OpPIDs
): List<MmSolveStatus> = transaction {
    find {
        (MmSolutionSchedules.mmUserId eq mmUser.id) and (MmSolutionSchedules.id inList opPIDs.map { it.value })
    }.map {
        MmSolveStatus(it.scheduleName, it.id.value, true)
    }
}

suspend inline fun MmProblemRequest.solve(
        solver: Solver<EventSchedule>,
        mmUser: MmUser,
        onOpPidCreated: (MmSolveStatus) -> Unit
): MmSolveStatus {
//    val client = HttpClient(Apache){
//        install(JsonFeature) {
//            serializer = GsonSerializer {
//                serializeNulls()
//                disableHtmlEscaping()
//            }
//        }
//    }
//    val response = client.post<HttpResponse>{
//        url("api.traveltimeapp.com")
//        contentType(ContentType.Application.Json)
//        body = TravelTimeRequest(emptyList(), emptyList(), emptyList())
//    }


    val pid = UUID.randomUUID().toString()
    val status = MmSolveStatus(scheduleName, pid, false)
    onOpPidCreated(status)

    // solve
    val unsolvedEventSchedule = EventSchedule(
            fixedEvents, toPlanEvents.map { it.toPlanning() }, userPreferences, currentTime
    )
    val solvedEventSchedule = withContext(Dispatchers.Default) {
        solver.solve(unsolvedEventSchedule)
    }

    // insert solved schedule into database
    val newSchedule = transaction {
        MmSolutionSchedule.new(pid) {
            this.scheduleName = this@solve.scheduleName
            this.mmUser = mmUser
        }
    }
    transaction {
        MmSolutionEvents.batchInsert(
                solvedEventSchedule.let { it.planningFlexibleEventList + it.userFixedEventList }
        ) {
            this[MmSolutionEvents.schedule] = newSchedule.id
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
    return status.copy(done = true)
}