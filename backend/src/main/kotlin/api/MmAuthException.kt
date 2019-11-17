package api

import io.ktor.application.ApplicationCall

/**
 * Custom exceptions made for micromanager.
 * Allows easier error logging.
 * @param [message] message of the exception.
 */
sealed class MmAuthException(
        message: String
) : RuntimeException(message) {
    open val jsonMessage: Map<String, Any?>
        get() = mapOf("OK" to false, "error" to message)
}

/**
 * Exception for invalid server authentication tokens.
 * @param [message] message of the exception.
 */
class ServerAuthTokenException(
        message: String
) : MmAuthException(message)


/**
 * Exception for when session is not found for an [ApplicationCall].
 * @param [message] message of the exception.
 */
class NoSessionException(
        message: String
): MmAuthException(message)