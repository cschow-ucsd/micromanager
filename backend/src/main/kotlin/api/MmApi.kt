package api

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.util.KtorExperimentalAPI
import util.mmSession


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
            call.respond(HttpStatusCode.OK)
        }
        get("/protected") {
            call.handleSession()
            call.respond("It's protected!")
        }
    }
}

private fun ApplicationCall.handleSession() {
    if (mmSession == null) throw NoSessionException("Missing session!")
}
