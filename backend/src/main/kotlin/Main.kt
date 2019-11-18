import com.typesafe.config.ConfigException
import io.ktor.server.netty.EngineMain
import io.ktor.util.KtorExperimentalAPI

/**
 * Entry point that starts the server.
 * Uses [EngineMain] with application.conf to start Netty server.
 */
@KtorExperimentalAPI
fun main(args: Array<String>) {
    println(sample.hello())
    try {
        EngineMain.main(args)
    } catch (e: ConfigException.UnresolvedSubstitution) {
        throw RuntimeException("Unresolved substitution in config. Missing reference.conf in resources?", e)
    }
}
