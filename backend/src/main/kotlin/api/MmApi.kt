package api

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.auth.OAuthAccessTokenResponse
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
fun Route.mmProtectedApi() = authenticate {
    route("/api") {
        handle {
            if (call.principal<OAuthAccessTokenResponse.OAuth2>() != null) {
                val mmSession = call.sessions.get<MmSession>() ?: MmSession(0)
                call.sessions.set(mmSession.copy(counter = mmSession.counter + 1))
            } else {
                call.respond(MmResponse.NoLogin)
            }
        }

        get("protected") {
            call.respond("It's protected!")
        }
    }
}

private suspend inline fun <R> ApplicationCall.errorAware(
        block: ApplicationCall.() -> R
): R? = try {
    if (sessions.get<MmSession>() != null || principal<OAuthAccessTokenResponse.OAuth2>() != null) {
        block()
    } else {
        respond(MmResponse.NoLogin)
        application.environment.log.debug("No logged in session found.")
        null
    }
} catch (e: Exception) {
    application.environment.log.error("Error: ${e.message}")
    null
}
