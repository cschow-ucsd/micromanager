package api

import BACKEND_OAUTH_CLIENT_ID
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.auth.Authentication
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.basic
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import mmDotenv
import org.apache.http.auth.AuthenticationException

/**
 * Verifies Google sign in tokens sent from client.
 */
private val googleIdTokenVerifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        NetHttpTransport(),
        JacksonFactory.getDefaultInstance()
).setAudience(listOf(mmDotenv.BACKEND_OAUTH_CLIENT_ID))
        .build()

/**
 * Custom session identifier.
 * Determines if a user has logged in.
 */
data class MmSession(
        val email: String,
        val counter: Int
)

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration() {
    basic {
        // skip if session exists
        skipWhen { it.sessions.get<MmSession>() != null }

        // authenticate if no session found
        realm = "Ktor Server"
        validate { credentials ->
            // authenticate token with Google
            val stringIdToken: String = credentials.password
            val idToken: GoogleIdToken = try {
                googleIdTokenVerifier.verify(stringIdToken)!!
            } catch (exception: Exception) {
                val specificException = when (exception) {
                    is KotlinNullPointerException -> AuthenticationException("Authentication failed, invalid token.")
                    is IllegalArgumentException -> IllegalArgumentException("Illegal arguments, invalid token.")
                    else -> exception
                }
                application.environment.log.debug(specificException.message)
                throw specificException
            }

            // successful if ID token is obtained
            application.environment.log.debug("Authentication successful.")
            return@validate UserIdPrincipal(idToken.payload.email)
        }
    }
}