package ucsd.ieeeqp.fa19.service

import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.HttpResponsePipeline
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class MmService(
        serverAuthToken: String
) {
    companion object {
        const val BASE_URL = "http://localhost:8080"
        private const val MICROMANAGER_SESSION = "MICROMANAGER_SESSION"
    }

    private var sessionHeader: String? = null
    private val client = HttpClient(Apache) {
        install(Auth) {
            basic {
                username = ""
                password = serverAuthToken
            }
        }
        install(JsonFeature) {
            serializer = GsonSerializer {
                serializeNulls()
                disableHtmlEscaping()
            }
        }
    }.apply {
        requestPipeline.intercept(HttpRequestPipeline.Before) {
            if (sessionHeader != null) context.header(MICROMANAGER_SESSION, sessionHeader)
        }
        responsePipeline.intercept(HttpResponsePipeline.After) {
            val sessionHeader = context.response.headers[MICROMANAGER_SESSION]
            if (sessionHeader != null) this@MmService.sessionHeader = sessionHeader
        }
    }

    fun getProtectedAsync(): Deferred<String> = client.async {
        val response = client.get<HttpResponse> {
            url(route("/api/protected"))
            contentType(ContentType.Application.Json)
        }
        return@async response.receive<String>()
    }

    private fun route(
            path: String
    ): String = "$BASE_URL$path"

    fun close() {
        client.close()
    }

    private fun HttpRequestBuilder.appendSessionHeader() {
        sessionHeader?.let { header("MICROMANAGER_SESSION", it) }
    }
}
