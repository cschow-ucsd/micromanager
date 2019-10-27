import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import org.slf4j.LoggerFactory

/**
 * Logger for API.
 * Reports API messages.
 */
private val logger = LoggerFactory.getLogger("MmApi")

fun Routing.mmApi() {
    val noLogin = "No login."
    get("/") {
        call.errorAware {
            // TODO: respond success
            respond("Hello World!")
        }
    }
    get("/test/{something}") {
        logger.error(call.parameters.toString())
        call.respond("Hi")
    }
}

private suspend inline fun <R> ApplicationCall.errorAware(
        block: ApplicationCall.() -> R
): R? = try {
    if (sessions.get<MmSession>() != null) {
        block()
    } else {
        respond(MmResponse.NoLogin)
        logger.debug("No logged in session found.")
        null
    }
} catch (e: Exception) {
    logger.error("Error: ${e.message}")
    null
}
