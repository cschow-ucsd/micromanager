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
import io.ktor.response.respondText
import io.ktor.routing.Routing
import io.ktor.routing.param
import io.ktor.routing.route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set

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
                clientId = dotenv["OAUTH_ANDROID_API_KEY"]!!,
                clientSecret = "", // no android secret key
                defaultScopes = listOf("profile")
        )
).associateBy { it.name }

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration(
        environment: ApplicationEnvironment
) {
    basic {
        skipWhen { it.sessions.get<MmSession>() != null }
    }
    oauth(MmAuthenticate.GOOGLE_OAUTH) {
        client = HttpClient(Apache).apply {
            environment.monitor.subscribe(ApplicationStopping) {
                close()
            }
        }
        providerLookup = { providers[parameters[MmAuthenticate.TYPE]] }
        urlProvider = { redirectUrl("/$MmAuthenticate.LOGIN") }
    }
}

/**
 * Authentication routing; logs in user.
 */
fun Routing.mmAuthenticate() = authenticate(MmAuthenticate.GOOGLE_OAUTH) {
    route("/${MmAuthenticate.LOGIN}/{${MmAuthenticate.TYPE}?}") {
        param("error") {
            handle {
                // TODO: Send JSON response of failed login
                call.respondText("Login failed")
            }
        }
        handle {
            val principle = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
            if (principle != null) {
                call.sessions.set(MmSession(principle.extraParameters["screen_name"] ?: ""))
                // TODO: Send JSON response of failed login
                call.respondText("Login successful!")
            } else {
                // TODO: Send JSON response of failed login
                call.respondText("Login failed")
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