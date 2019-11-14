package api

import MmHoconConfig
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.directorySessionStorage
import io.ktor.sessions.header
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import java.io.File

/**
 * Functionality injected into the request/response pipelines.
 * E.g. sessions to persist data between calls
 * E.g. user authentication
 */
@KtorExperimentalAPI
fun Application.installFeatures() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
    install(Sessions) {
        header<MmSession>(
                name = "MICROMANAGER_SESSION",
                storage = directorySessionStorage(File(".sessions"))
        ) {
            val hex = hex(MmHoconConfig.mmTransformerHex)
            transform(SessionTransportTransformerMessageAuthentication(hex))
        }
    }
    install(StatusPages) {
        mmStatusPagesConfiguration()
    }
    install(Authentication) {
        mmAuthConfiguration()
    }
}