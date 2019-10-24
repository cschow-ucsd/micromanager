import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.*

/**
 * Handles the installs of features and routing of all API calls.
 * Also handles logins.
 */
fun Application.main() {

    install(Sessions) {
        cookie<MmSession>("ktorOAuthSessionId", SessionStorageMemory()) {
            cookie.path = "/"
        }
    }
    install(Authentication) {
        mmOAuthConfiguration(environment)
    }

    routing {

        fun ApplicationCall.mmSession() = sessions.get<MmSession>()
        suspend fun ApplicationCall.respondIfAuth(
                block: suspend (MmSession) -> Unit
        ) {
            sessions.get<MmSession>()?.let { block }
                    ?: respondText("Did not login") // TODO: respond did not login
        }

        get("/") {
            call.mmSession()?.let {
                println("Session ID: ${it.userId}")
                call.respondText("Some data") // TODO: respond appropriate data
            } ?: call.respondText("Did not login")
        }
        mmAuthenticate()
    }
}

