package api

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.basicAuthenticationCredentials
import io.ktor.auth.principal
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
fun Route.mmPublicApi() {
    get("/") {
        call.respond("Hello World!")
    }
    get("/echotoken") {
        val token: String? = call.request.basicAuthenticationCredentials()?.password
        call.application.environment.log.debug("echotoken: $token")
        call.respond(token ?: "")
    }
}

/**
 * Protected routes.
 * Can only be accessed if session exists or logged in.
 */
fun Route.mmProtectedApi() = authenticate(MmAuthenticate.API_AUTH) {
    route("/api") {
        get("/protected") {
            call.handleSession()
            call.respond("It's protected!")
        }
    }
}

private fun ApplicationCall.handleSession() {
    val principal = principal<MmPrincipal>()
    val mmSession = sessions.get<MmSession>()
    if (principal == null && mmSession == null) {
        throw NoPrincipalException("Missing principal!")
    }
    sessions.set(mmSession?.copy(count = mmSession.count + 1)
            ?: MmSession(principal!!.expiration, principal.subject, 0))
}
