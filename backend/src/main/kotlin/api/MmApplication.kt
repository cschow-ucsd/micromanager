package api

import exposed.MmDatabase
import io.ktor.application.Application
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI

/**
 * Handles the installs of features and routing of all API calls.
 * Also handles logins.
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

