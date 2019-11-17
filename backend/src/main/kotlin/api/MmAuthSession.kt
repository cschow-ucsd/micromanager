package api

/**
 * Custom session identifier.
 * Determines if a user has logged in.
 */
data class MmSession(
        val subject: String,
        val expireAt: Long,
        val accessToken: String
)

val MmSession.isExpired: Boolean
    get() = System.currentTimeMillis() > expireAt

val MmSession.debugInfo: String
    get() = "Subject: $subject; Expired: $isExpired"