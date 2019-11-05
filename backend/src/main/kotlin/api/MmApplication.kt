package api

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.routing
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.header
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import org.apache.http.auth.AuthenticationException
import java.lang.IllegalArgumentException

/**
 * Handles the installs of features and routing of all API calls.
 * Also handles logins.
 */
@KtorExperimentalAPI
fun Application.mmMain() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
    install(Sessions) {
        header<MmSession>("KTOR_OAUTH_SESSION", SessionStorageMemory()) {
            val hex = hex(mmDotenv.BACKEND_TRANSFORMER_HEX)
            transform(SessionTransportTransformerMessageAuthentication(hex))
        }
    }
    install(StatusPages) {
        exception<AuthenticationException> {
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
        }
    }
    install(Authentication) {
        mmOAuthConfiguration()
    }

    routing {
        mmPublicApi()
        mmProtectedApi()
    }
}

