package api

import com.google.api.client.auth.openidconnect.IdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.application.log
import io.ktor.auth.Authentication
import io.ktor.auth.OAuthAccessTokenResponse
import io.ktor.auth.basic
import io.ktor.features.callId
import io.ktor.sessions.get
import io.ktor.sessions.sessions

/**
 * Verifies Google sign in tokens sent from client.
 */
val googleIdTokenVerifier: GoogleIdTokenVerifier = GoogleIdTokenVerifier.Builder(
        NetHttpTransport(),
        JacksonFactory.getDefaultInstance()
).setAudience(listOf(mmDotenv.BACKEND_OAUTH_CLIENT_ID))
        .build()

/**
 * Custom session identifier.
 * Determines if a user has logged in.
 */
data class MmSession(
        val counter: Int
)

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration() {
    basic {
        skipWhen { it.sessions.get<MmSession>() != null }
    }
    basic {
        realm = "Ktor Server"
        validate { credentials ->

            // authenticate token with Google
            val stringIdToken = credentials.password
            val idToken: GoogleIdToken? = googleIdTokenVerifier.verify(stringIdToken)

            // log to indicate success or failure
            application.environment.log.debug(
                    if (idToken != null) "Authentication Successful"
                    else "Authentication failed"
            )

            // create an OAuth token based on GoogleIdToken response
            return@validate idToken?.payload?.let {
                OAuthAccessTokenResponse.OAuth2(
                        accessToken = it.accessTokenHash,
                        tokenType = it.type,
                        expiresIn = it.expirationTimeSeconds,
                        refreshToken = null
                )
            }
        }
    }
}

/**
 * Authentication routing; logs in user.
 */
//fun Route.mmAuthenticate() = authenticate {
//    param("error") {
//        handle {
//            call.respond(MmResponse.LoginFailed)
//            logger.debug("User login failed 1.")
//        }
//    }
//    handle {
//        val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
//        if (principal == null) {
//            call.respond(MmResponse.LoginFailed)
//            logger.debug("User login failed 2.")
//        } else {
//            logger.debug("User login successful.")
//        }
//    }
//    mmProtectedApi()
//}