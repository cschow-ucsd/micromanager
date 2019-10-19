import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val server = embeddedServer(
            factory = Netty,
            port = 8080,
            module = Application::main
    )
    server.start(wait = true)
}

