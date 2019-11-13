package api

import com.google.api.client.auth.oauth2.TokenResponseException
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import exposed.dao.MmUser
import exposed.dsl.MmUsers
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
 * Authentication constants.
 */
object MmAuthenticate {
    const val API_AUTH = "ApiAuth"
}

/**
 * Authentication configuration.
 */
fun Authentication.Configuration.mmAuthConfiguration() {
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

/**
 * Basic authentication.
 * Server authentication code is sent by password in [credentials].
 * @param [credentials] that contains the server authentication code.
 * @return [Principal] to show that the user has been authenticated.
 */
private suspend fun ApplicationCall.basicValidation(
        credentials: UserPasswordCredential
): Principal? = when {
    mmSession == null -> authWithServerAuthCode(credentials)
    mmSession!!.isExpired -> transaction { MmUser[mmSession!!.subject] }
            .refreshToken?.let {
        val (tokenResponse, idToken) = makeGoogleAuthRequest(it).await()
        createSession(tokenResponse, idToken)
        UserIdPrincipal(idToken.payload.subject)
    } ?: authWithServerAuthCode(credentials)
    else -> UserIdPrincipal(mmSession!!.subject) // Nothing wrong with this token
}

/**
 * Implementation of Google's server auth code flow.
 * Client sends server auth code -> server exchanges auth code for access & refresh tokens.
 * @param [credentials] that contains the server authentication code.
 * @return [UserIdPrincipal] to show that the user has been authenticated.
 */
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

/**
 * Creates a new user in the [MmUsers] database.
 * @param [tokenResponse] response from Google containing the access & refresh tokens.
 * @param [idToken] containing the user's profile information.
 */
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

/**
 * Creates a new session to persist data between calls.
 * Allows clients to save session information to skip authentication in subsequent calls.
 * @param [tokenResponse] response from Google containing the access & refresh tokens.
 * @param [idToken] containing the user's profile information.
 */
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
 * @param [authCode] client authentication code to verify with Google.
 * @return [GoogleAuthorizationCodeTokenRequest] to be sent to Google for authentication.
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

/**
 * Helper function to wait for blocking authentication token requests.
 * @return pair of [GoogleTokenResponse] & [GoogleIdToken] after validated.
 */
private suspend fun GoogleAuthorizationCodeTokenRequest.await(
): Pair<GoogleTokenResponse, GoogleIdToken> = suspendCoroutine { continuation ->
    thread(start = true) {
        try {
            val tokenResponse: GoogleTokenResponse = execute()!!
            val idToken = tokenResponse.parseIdToken()!!
            continuation.resume(tokenResponse to idToken)
        } catch (e: TokenResponseException) {
            continuation.resumeWithException(ServerAuthTokenException("Server authentication token invalid."))
        } catch (e: Exception) {
            continuation.resumeWithException(ServerAuthTokenException("Something went wrong validating token."))
        }
    }
}