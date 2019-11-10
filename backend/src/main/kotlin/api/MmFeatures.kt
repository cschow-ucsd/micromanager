package api

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.gson.gson
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.header
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex
import sessions.MmSessionData
import sessions.MmSessionStorage
import util.BACKEND_TRANSFORMER_HEX
import util.mmDotenv

@KtorExperimentalAPI
fun Application.installFeatures() {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            disableHtmlEscaping()
        }
    }
    install(Sessions) {
        header<MmSessionData>("MICROMANAGER_SESSION", MmSessionStorage()) {
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