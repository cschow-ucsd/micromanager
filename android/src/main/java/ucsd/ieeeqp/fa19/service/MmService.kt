package ucsd.ieeeqp.fa19.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MmService(
        private val idToken: String
) : CoroutineScope {

    companion object {
        const val BASE_URL = "http://localhost:8080"
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Default + Job()
    private val client = HttpClient(Apache) {
        install(Auth) {
            basic {
                username = ""
                password = idToken // token is basically just a password without username
            }
        }
        install(JsonFeature) {
            serializer = GsonSerializer {
                serializeNulls()
                disableHtmlEscaping()
            }
        }
    }

    fun getProtectedAsync(
            callback: MmResponseCallback<String>
    ) = errorAwareLaunch(callback) {
        val response = client.get<String> {
            url(route("/api/protected"))
            contentType(ContentType.Application.Json)
        }
        callback.handleResponse(response)
    }

    private fun route(
            path: String
    ): String = "$BASE_URL$path"

    private fun <T> CoroutineScope.errorAwareLaunch(
            callback: MmResponseCallback<T>,
            block: suspend CoroutineScope.() -> Unit
    ) = launch {
        try {
            block()
        } catch (t: Throwable) {
            callback.handleError(t)
        }
    }

    fun close() {
        coroutineContext.cancel()
        client.close()
    }
}
