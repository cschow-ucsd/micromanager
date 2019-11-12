package api

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
import util.BACKEND_TRANSFORMER_HEX
import util.mmDotenv
import java.io.File

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
                storage = directorySessionStorage(File("data/.sessions"))
        ) {
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
}