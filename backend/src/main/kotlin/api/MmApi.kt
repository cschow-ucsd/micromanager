package api

import call.MmProblemRequest
import call.MmSolutionResponse
import call.OpPID
import call.OpPIDs
import exposed.dao.MmSolutionEvent
import exposed.dsl.MmSolutionEvents
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import optaplanner.BaseFixedEvent
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import util.mmSession

/**
 * Public routes.
 * Can be accessed by directly calling the endpoints.
 */
@KtorExperimentalAPI
fun Route.mmPublicApi() {
    get("/") {
        call.respondText("Hello World!")
    }
}

/**
 * Protected routes.
 * Can only be accessed if session exists or logged in.
 */
fun Route.mmProtectedApi() = authenticate(MmAuthenticate.API_AUTH) {
    route("/api") {
        get("/login") {
            call.handleSession()
            call.respond(HttpStatusCode.OK)
        }
        post("/op-solve") {
            call.handleSession()
            val problem = call.receive<MmProblemRequest>()
            TODO("solve the problem asynchronously, return an 202 Accepted response")
//            call.respond(
//                    status = HttpStatusCode.Accepted,
//                    message = MmSolveAccepted(TODO(), TODO())
//            )
        }
        get("/status") {
            call.handleSession()
            val mmPIDs = call.receive<OpPIDs>()
            TODO("retrieve status of the problems with list of PIDs")
//            val response: MmStatusResponse = mmPIDs.map {
//                MmSolveAccepted(TODO(), TODO())
//            }
//            call.respond(
//                    status = HttpStatusCode.Accepted,
//                    message = response
//            )
        }
        get("/solution") {
            call.handleSession()
            val subject = call.mmSession!!.subject
            val opPID = call.receive<OpPID>()
            val solutionEvents = transaction {
                MmSolutionEvent.find { (MmSolutionEvents.mmUser eq subject) and (MmSolutionEvents.opPID eq opPID) }
            }
            val fixedEvents = mutableListOf<BaseFixedEvent>()
            val plannedEvents = mutableListOf<BaseFixedEvent>()
            solutionEvents.forEach {
                if (it.isOpPlanned) plannedEvents.add(it)
                else fixedEvents.add(it)
            }
            call.respond(HttpStatusCode.OK, MmSolutionResponse(fixedEvents, plannedEvents))
        }
    }
}

/**
 * Checks if a session exists before handling the API calls.
 */
private fun ApplicationCall.handleSession() {
    if (mmSession == null) throw NoSessionException("Missing session!")
}
