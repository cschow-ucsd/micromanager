package ucsd.ieeeqp.fa19.service

import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.client.response.HttpReceivePipeline

object MmHttpClient {
    const val MICROMANAGER_SESSION = "MICROMANAGER_SESSION"

    fun create(
            serverAuthCode: String?,
            sessionAuthHeader: String?,
            sessionChangedListener: (String) -> Unit
    ): HttpClient {
        val client = HttpClient(Apache) {
            install(Auth) {
                basic {
                    username = ""
                    password = serverAuthCode ?: ""
                }
            }
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }
        var sessionHeader = sessionAuthHeader
        client.requestPipeline.intercept(HttpRequestPipeline.Before) {
            if (sessionHeader != null) context.header(MICROMANAGER_SESSION, sessionHeader)
        }
        client.receivePipeline.intercept(HttpReceivePipeline.After) {
            val newSessionHeader = context.response.headers[MICROMANAGER_SESSION]
            if (newSessionHeader != null) {
                sessionHeader = newSessionHeader
                sessionChangedListener(newSessionHeader)
            }
        }
        return client
    }
}