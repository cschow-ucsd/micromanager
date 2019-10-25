import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.features.ContentNegotiation
import io.ktor.features.callId
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.sessions.*
import io.ktor.util.pipeline.PipelineContext

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
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        mmApi()
        mmAuthenticate()
    }
}

