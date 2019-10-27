import MmAuthenticate.GOOGLE_OAUTH
import MmAuthenticate.LOGIN
import MmAuthenticate.TYPE
import io.github.cdimascio.dotenv.dotenv
import io.ktor.application.ApplicationCall
import io.ktor.application.ApplicationEnvironment
import io.ktor.application.ApplicationStopping
import io.ktor.application.call
import io.ktor.auth.*
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.origin
import io.ktor.http.HttpMethod
import io.ktor.request.host
import io.ktor.request.port
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.param
import io.ktor.routing.route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import org.slf4j.LoggerFactory

/**
 * Logger for authentication.
 * Reports authentication messages.
 */
private val logger = LoggerFactory.getLogger("MmAuthenticate")

/**
 * Gets API Keys from .env file in root of project.
 * Need to make new .env file every time this project is cloned.
 */
private val dotenv = dotenv()

/**
 * Lists login providers, e.g. Google, Facebook
 * Different clients (e.g. Android, web) have different settings
 */
private val providers = listOf(
        OAuthServerSettings.OAuth2ServerSettings(
                name = "android", // android client
                authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
                accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
                requestMethod = HttpMethod.Post,
                clientId = dotenv["OAUTH_ANDROID_API_KEY"]!!.also { logger.debug("OAuth: Found Android API Key.") },
                clientSecret = "", // no android secret key
                defaultScopes = listOf("profile", "https://www.googleapis.com/auth/calendar")
        )
).associateBy { it.name }

/**
 * Authentication constants.
 */
object MmAuthenticate {
    const val GOOGLE_OAUTH = "google-oauth"
    const val TYPE = "type"
    const val LOGIN = "login"

}

/**
 * Custom session identifier.
 * Determines if a user has logged in.
 */
inline class MmSession(val userId: String)

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration(
        environment: ApplicationEnvironment
) {
    basic {
        skipWhen { it.sessions.get<MmSession>() != null }
    }
    oauth(GOOGLE_OAUTH) {
        val authType: ApplicationCall.() -> String? = { parameters[TYPE] }

        client = HttpClient(Apache).apply {
            environment.monitor.subscribe(ApplicationStopping) {
                close()
            }
        }
        providerLookup = { providers[authType()] }
        urlProvider = { redirectUrl("/$LOGIN/${authType()}") }
    }
}

/**
 * Authentication routing; logs in user.
 */
fun Routing.mmAuthenticate() = authenticate(GOOGLE_OAUTH) {
    route("/$LOGIN/{$TYPE}") {
        param("error") {
            handle {
                call.respond(MmResponse.LoginFailed)
                logger.debug("User login failed 1.")
            }
        }
        handle {
            // checks if user is authenticated
            val principle = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
            if (principle != null) {
                call.sessions.set(MmSession(principle.extraParameters["screen_name"] ?: ""))
                call.respond(MmResponse.LoginSuccess)
                logger.debug("User login success!")
            } else {
                call.respond(MmResponse.LoginFailed)
                logger.debug("User login failed 2.")
            }
        }
    }
}

private fun ApplicationCall.redirectUrl(path: String): String {
    val defaultPort = if (request.origin.scheme == "http") 80 else 443
    val hostPort = request.host() + request.port().let { port -> if (port == defaultPort) "" else ":$port" }
    val protocol = request.origin.scheme
    return "$protocol://$hostPort$path"
}