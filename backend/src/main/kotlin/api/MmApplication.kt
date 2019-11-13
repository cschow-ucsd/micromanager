package api

import exposed.MmDatabase
import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

/**
 * Main structure of the API.
 * 1. Sets up database
 * 2. Install features that the API uses
 * 3. Route the endpoints.
 */
@KtorExperimentalAPI
fun Application.mmMain() {
    MmDatabase.setup()
    installFeatures()

    routing {
        mmPublicApi()
        mmProtectedApi()
    }
}

