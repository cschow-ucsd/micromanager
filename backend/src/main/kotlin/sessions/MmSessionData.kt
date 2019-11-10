package sessions

/**
 * Custom session identifier.
 * Determines if a user has logged in.
 */
data class MmSessionData(
        val subject: String,
        val expireAt: Long,
        val accessToken: String
)

val MmSessionData.isExpired: Boolean
    get() = System.currentTimeMillis() > expireAt

val MmSessionData.debugInfo: String
    get() = "Subject: $subject; Expired: $isExpired"