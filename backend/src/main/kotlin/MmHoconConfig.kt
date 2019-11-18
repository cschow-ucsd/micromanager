import com.typesafe.config.ConfigFactory
import io.ktor.config.HoconApplicationConfig
import io.ktor.util.KtorExperimentalAPI

@KtorExperimentalAPI
object MmHoconConfig : HoconApplicationConfig(ConfigFactory.load()) {
    operator fun get(key: String) = property(key).getString()

    val dbUrl = this["db.jdbcUrl"]
    val dbUser = this["db.dbUser"]

    val dbPassword = this["db.dbPassword"]
    val mmClientId = this["mm.clientId"]
    val mmClientSecret = this["mm.clientSecret"]
    val mmTransformerHex = this["mm.transformerHex"]
}

/**
 * Loads application configuration from .conf files in resources
 * When cloned
 */
//private fun loadMmConfig(): Config =
//        ConfigFactory.parseResources("POSTGRES_MM_URL = \"jdbc:postgresql://localhost:5432/micromanager\"\nPOSTGRES_USER = \"postgres\"\nPOSTSGRES_PASSWORD = \"kyroschow\"\n\nMM_CLIENT_SECRET = \"uYFVhIXwtaMsrrvzWaptTaRj\"\nMM_TRANSFORMER_HEX = \"da4321dded2b27ce1c47baa18f91a1\"\n")
//        .withFallback(ConfigFactory.parseResources("application.conf"))