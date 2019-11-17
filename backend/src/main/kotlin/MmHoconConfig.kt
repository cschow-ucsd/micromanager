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