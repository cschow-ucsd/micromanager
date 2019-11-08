package api

sealed class MmException(
        message: String
) : RuntimeException(message) {
    open val jsonMessage: Map<String, Any?>
        get() = mapOf("OK" to false, "error" to message)
}

class ServerAuthTokenException(
        message: String
) : MmException(message)

class GoogleTokenException(
        message: String
) : MmException(message)

class NoSessionException(
        message: String
): MmException(message)