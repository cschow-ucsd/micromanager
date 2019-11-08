package api

import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.application.ApplicationCall
import io.ktor.auth.*
import util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MmAuthenticate {
    const val API_AUTH = "ApiAuth"
}

/**
 * Custom session identifier.
 * Determines if a user has logged in.
 */
data class MmSession(
        val expireAt: Long,
        val accessToken: String,
        val email: String,
        val subject: String,
        val count: Int
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > expireAt
}

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration() {
    basic(MmAuthenticate.API_AUTH) {
        // skip if session exists
        skipWhen {
            if (it.mmSession == null) return@skipWhen false
            it.logger.debug("Found session. Subject: ${it.mmSession!!.subject}; Expired: ${it.mmSession!!.isExpired}")
            return@skipWhen !it.mmSession!!.isExpired
        }

        realm = "Ktor Server"
        validate { basicValidation(it) }
    }
}

private suspend fun ApplicationCall.basicValidation(
        credentials: UserPasswordCredential
): Principal? {
    val oldSession = this.mmSession
    return when {
        oldSession == null -> {
            // authenticate this new user
            val serverAuthToken: String = credentials.password
            val (tokenResponse, idToken) = makeGoogleAuthRequest(serverAuthToken).await()

            // create principal and session
            val expireMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(tokenResponse.expiresInSeconds)
            this.mmSession = try {
                MmSession(
                        expireAt = expireMillis,
                        accessToken = tokenResponse.accessToken,
                        email = idToken.payload.email,
                        subject = idToken.payload.subject,
                        count = 0
                ).also {
                    logger.debug("Authentication Successful. Subject: ${it.subject}; Email: ${it.email}")
                }
            } catch (e: NullPointerException) {
                throw GoogleTokenException("Invalid Google ID token; missing some information (e.g. email)?")
            }
            UserIdPrincipal(idToken.payload.subject)
        }
        oldSession.isExpired -> {
            TODO("Use Refresh token to get new access token")
        }
        else -> {
            // Nothing wrong with this token
            UserIdPrincipal(oldSession.subject)
        }
    }
}

/**
 * Verifies Google sign in tokens sent from client.
 */
private fun makeGoogleAuthRequest(
        authCode: String
) = GoogleAuthorizationCodeTokenRequest(
        NetHttpTransport(),
        JacksonFactory.getDefaultInstance(),
        "https://oauth2.googleapis.com/token",
        mmDotenv.BACKEND_OAUTH_CLIENT_ID,
        mmDotenv.BACKEND_OAUTH_CLIENT_SECRET,
        authCode,
        "" // no redirect URL needed
)

private suspend fun GoogleAuthorizationCodeTokenRequest.await(
): Pair<GoogleTokenResponse, GoogleIdToken> = suspendCoroutine { continuation ->
    thread(start = true) {
        try {
            val tokenResponse: GoogleTokenResponse = execute()!!
            val idToken = tokenResponse.parseIdToken()!!
            continuation.resume(tokenResponse to idToken)
        } catch (e: TokenResponseException) {
            continuation.resumeWithException(ServerAuthTokenException("Server authentication token invalid."))
        } catch (e: NullPointerException) {
            continuation.resumeWithException(ServerAuthTokenException("Server authentication token is null."))
        }
    }
}