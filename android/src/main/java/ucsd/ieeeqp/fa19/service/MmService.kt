package ucsd.ieeeqp.fa19.service

import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class MmService(
        serverAuthCode: String?,
        sessionAuthHeader: String?,
        sessionChangedListener: (String) -> Unit
) {

    private var sessionHeader: String? = null
    private val client = MmHttpClient.create(serverAuthCode, sessionAuthHeader, sessionChangedListener)

    fun getProtectedAsync(): Deferred<String> = client.async {
        client.get<String>(route("/api/protected"))
    }

    fun loginAsync(): Deferred<Boolean> = client.async {
        client.get<Boolean>(route("/api/login"))
    }

    private fun route(
            path: String
    ): String = "${MmHttpClient.BASE_URL}/$path"

    fun close() {
        client.close()
    }

    private fun HttpRequestBuilder.appendSessionHeader() {
        sessionHeader?.let { header("MICROMANAGER_SESSION", it) }
    }
}
