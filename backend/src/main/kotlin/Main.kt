import io.ktor.server.netty.EngineMain

/**
 * Entry point that starts the server.
 * Uses [EngineMain] with application.conf to start Netty server.
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)