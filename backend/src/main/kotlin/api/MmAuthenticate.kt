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
            if (it.mmSession == null) {
                it.logger.debug("Null session, cannot skip.")
                return@skipWhen false
            }
            it.logger.debug("Found existing session. ${it.mmSession!!.debugInfo}")
            return@skipWhen !it.mmSession!!.isExpired
        }

        realm = "Ktor Server"
        validate { basicValidation(it) }
    }
}

private suspend fun ApplicationCall.basicValidation(
        credentials: UserPasswordCredential
): Principal? = when {
    mmSession == null -> authWithServerAuthCode(credentials)
    mmSession!!.isExpired -> {
        val existingUser = transaction { MmUser[mmSession!!.subject] }
        if (existingUser.refreshToken != null) {
            val (tokenResponse, idToken) = makeGoogleAuthRequest(existingUser.refreshToken!!).await()
            createSession(tokenResponse, idToken)
            UserIdPrincipal(idToken.payload.subject)
        } else {
            authWithServerAuthCode(credentials)
        }
    }
    else -> UserIdPrincipal(mmSession!!.subject) // Nothing wrong with this token
}

private suspend fun ApplicationCall.authWithServerAuthCode(
        credentials: UserPasswordCredential
): UserIdPrincipal {
    val serverAuthToken: String = credentials.password
    val (tokenResponse, idToken) = makeGoogleAuthRequest(serverAuthToken).await()

    val existingUser = transaction { MmUser.findById(idToken.payload.subject) }
    if (existingUser == null) {
        createNewUser(tokenResponse, idToken)
    }
    createSession(tokenResponse, idToken)
    return UserIdPrincipal(idToken.payload.subject)
}

private fun ApplicationCall.createNewUser(
        tokenResponse: GoogleTokenResponse,
        idToken: GoogleIdToken
) {
    if (tokenResponse.refreshToken == null) {
        logger.debug("WARNING: No refresh token found for new user. Server auth code must be supplied every time.")
    }
    transaction {
        MmUser.new(idToken.payload.subject) {
            email = idToken.payload.email
            refreshToken = tokenResponse.refreshToken
        }
    }
}

private fun ApplicationCall.createSession(
        tokenResponse: GoogleTokenResponse,
        idToken: GoogleIdToken
) {
    val existingUser = transaction { MmUser[idToken.payload.subject] }
    mmSession = MmSession(
            subject = existingUser.id.value,
            expireAt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(tokenResponse.expiresInSeconds),
            accessToken = tokenResponse.accessToken
    )
    logger.debug("Authentication successful. Subject: ${mmSession!!.subject}")
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