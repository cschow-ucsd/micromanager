package api

sealed class MmAuthException(
        message: String
) : RuntimeException(message) {
    open val jsonMessage: Map<String, Any?>
        get() = mapOf("OK" to false, "error" to message)
}

class ServerAuthTokenException(
        message: String
) : MmAuthException(message)

class GoogleTokenException(
        message: String
) : MmAuthException(message)

class NoSessionException(
        message: String
): MmAuthException(message)