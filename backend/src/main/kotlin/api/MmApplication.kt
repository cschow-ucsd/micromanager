package api

import BACKEND_TRANSFORMER_HEX
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.routing.routing
import io.ktor.sessions.SessionStorageMemory
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.header
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import mmDotenv

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
        header<MmSession>("MICROMANAGER_SESSION", SessionStorageMemory()) {
            val hex = hex(mmDotenv.BACKEND_TRANSFORMER_HEX)
            transform(SessionTransportTransformerMessageAuthentication(hex))
        }
    }
    install(StatusPages) {
        mmStatusPagesConfiguration()
    }
    install(Authentication) {
        mmOAuthConfiguration()
    }

    routing {
        mmPublicApi()
        mmProtectedApi()
    }
}

