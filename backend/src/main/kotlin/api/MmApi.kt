package api

import call.*
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
            val problem = call.receive<MmProblemRequest>()
            // TODO: solve the problem asynchronously, return an 202 Accepted response
            call.respond(
                    status = HttpStatusCode.Accepted,
                    message = MmSolveAccepted(TODO(), TODO())
            )
        }
        get("/status") {
            val mmPIDs = call.receive<MmPIDs>()
            // TODO: retrieve status of the problem with pid
            call.respond(
                    status = HttpStatusCode.Accepted,
                    message = MmSolveAccepted(TODO(), TODO())
            )
        }
        get("/solution") {
            val mmPID = call.receive<MmPID>()
            // TODO: retrieve solution of the problem with pid
            call.respond(
                    status = HttpStatusCode.OK,
                    message = MmSolutionResponse(TODO(), TODO())
            )
        }
    }
}

/**
 * Checks if a session exists before handling the API calls.
 */
private fun ApplicationCall.handleSession() {
    if (mmSession == null) throw NoSessionException("Missing session!")
}
