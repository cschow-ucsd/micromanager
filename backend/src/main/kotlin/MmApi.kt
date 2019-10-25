import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.sessions.get
import io.ktor.sessions.sessions

fun Routing.mmApi() {
    val noLogin = "No login."
    get("/") {
        call.errorAware {
            // TODO: respond success
            call.respond("Hello World!")
        }
    }
}

private suspend inline fun <R> ApplicationCall.errorAware(
        block: suspend (ApplicationCall) -> R
): R? = try {
    requireNotNull(sessions.get<MmSession>()) {
        MmResponse.NoLogin.apply { respond(this) }
    }
    block(this)
} catch (e: Exception) {
    println("Error: ${e.message}")
    null
}

private val ApplicationCall.session: MmSession?
    get() = sessions.get<MmSession>()
