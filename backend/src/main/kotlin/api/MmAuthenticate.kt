package api

import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import exposed.dao.MmUser
import io.ktor.application.ApplicationCall
import io.ktor.auth.*
import org.jetbrains.exposed.sql.transactions.transaction
import sessions.MmSessionData
import sessions.debugInfo
import sessions.isExpired
import util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Constants
 */
object MmAuthenticate {
    const val API_AUTH = "ApiAuth"
}

/**
 * Configuration settings for OAuth.
 */
fun Authentication.Configuration.mmOAuthConfiguration() {
    basic(MmAuthenticate.API_AUTH) {
        // skip if session exists
        skipWhen {
            if (it.mmSessionData == null) return@skipWhen false
            it.logger.debug("Found existing session. ${it.mmSessionData!!.debugInfo}")
            return@skipWhen !it.mmSessionData!!.isExpired
        }

        realm = "Ktor Server"
        validate { basicValidation(it) }
    }
}

private suspend fun ApplicationCall.basicValidation(
        credentials: UserPasswordCredential
): Principal? = when {
    mmSessionData == null -> {
        // authenticate this new user
        val serverAuthToken: String = credentials.password
        val (tokenResponse, idToken) = makeGoogleAuthRequest(serverAuthToken).await()

        if (tokenResponse.refreshToken != null) {
            createNewUser(tokenResponse, idToken)
        }
        createSession(tokenResponse, idToken)
        UserIdPrincipal(idToken.payload.subject)
    }
    mmSessionData!!.isExpired -> {
        val existingUser = transaction { MmUser[mmSessionData!!.subject] }
        val (tokenResponse, idToken) = makeGoogleAuthRequest(existingUser.refreshToken).await()
        createSession(tokenResponse, idToken)
        UserIdPrincipal(idToken.payload.subject)
    }
    else -> {
        // Nothing wrong with this token
        UserIdPrincipal(mmSessionData!!.subject)
    }
}

private fun createNewUser(
        tokenResponse: GoogleTokenResponse,
        idToken: GoogleIdToken
): Unit = transaction {
    MmUser.new(idToken.payload.subject) {
        email = idToken.payload.email
        refreshToken = tokenResponse.refreshToken
    }
}

private fun ApplicationCall.createSession(
        tokenResponse: GoogleTokenResponse,
        idToken: GoogleIdToken
) {
    val existingUser = transaction { MmUser[idToken.payload.subject] }
    mmSessionData = MmSessionData(
            subject = existingUser.id.value,
            expireAt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(tokenResponse.expiresInSeconds),
            accessToken = tokenResponse.accessToken
    )
    logger.debug("Authentication successful. Subject: ${mmSessionData!!.subject}")
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