package api

import BACKEND_OAUTH_CLIENT_ID
import BACKEND_OAUTH_CLIENT_SECRET
import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.auth.Authentication
import io.ktor.auth.Principal
import io.ktor.auth.basic
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import mmDotenv
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
        val expiration: Long,
        val subject: String,
        val count: Int
) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > expiration
}

data class MmPrincipal(
        val email: String,
        val subject: String,
        val accessToken: String,
        val expiration: Long
) : Principal

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration() {
    basic(MmAuthenticate.API_AUTH) {
        // skip if session exists
        skipWhen {
            val mmSession = it.sessions.get<MmSession>() ?: return@skipWhen false
            it.application.environment.log.debug("Found session. Subject: ${mmSession.subject}; Expired: ${mmSession.isExpired}")
            return@skipWhen !mmSession.isExpired
        }

        // authenticate if no session found
        realm = "Ktor Server"
        validate { credentials ->

            // get server authentication token from client
            val serverAuthToken: String = credentials.password

            // obtain access code using server authentication token
            val (tokenResponse, idToken) = makeGoogleAuthRequest(serverAuthToken).await()

            // create principal
            return@validate try {
                MmPrincipal(
                        email = idToken.payload.email,
                        subject = idToken.payload.subject,
                        accessToken = tokenResponse.accessToken,
                        expiration = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(tokenResponse.expiresInSeconds)
                ).also {
                    // log to show user successfully logged in
                    application.environment.log.debug("Authentication Successful. Subject: ${it.subject}; Email: ${it.email}")
                }
            } catch (e: NullPointerException) {
                throw GoogleTokenException("Invalid Google ID token; missing some information (e.g. email)?")
            }
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
        } catch (e: NullPointerException) {
            continuation.resumeWithException(ServerAuthTokenException("Server authentication token is null."))
        }
    }
}