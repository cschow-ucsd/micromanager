import api.mmMain
import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI

/**
 * Entry point that starts the server.
 */
@KtorExperimentalAPI
fun main() {
    val server = embeddedServer(
            factory = Netty,
            port = 8080,
            module = Application::mmMain
    )
    server.start(wait = true)
}
