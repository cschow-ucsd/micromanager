import api.MmResponse
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
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ApiCallTest {
    private lateinit var client: HttpClient

    @BeforeTest
    fun setup() {
        client = HttpClient(Apache) {
            install(Auth) {
                basic {
                    username = ""
                        password = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjljZWY1MzQwNjQyYjE1N2ZhOGE0ZjBkODc0ZmU3OTAwMzYyZDgyZGIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxNjgwMzc4NDAyNDctczY1M2k0ZDA5OWU1am90bnFldWNtMGw1dm81ZzB0ZmEuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxNjgwMzc4NDAyNDctYW51NGhmb2J1cXVjbW5wZnNmbHZmYm5yZ2pjcDRtOWkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDMwNDkxMDY1OTQ3MDg0NjIxMjciLCJoZCI6InVjc2QuZWR1IiwiZW1haWwiOiJjc2Nob3dAdWNzZC5lZHUiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IkNoaSBDaG93IiwicGljdHVyZSI6Imh0dHBzOi8vbGg1Lmdvb2dsZXVzZXJjb250ZW50LmNvbS8tb1ZqMzkzSTlvMkUvQUFBQUFBQUFBQUkvQUFBQUFBQUFBQUEvQUNIaTNyZFhSODNGZU9hclBFd2tWQ0NqMjU2bHZtSVdRZy9zOTYtYy9waG90by5qcGciLCJnaXZlbl9uYW1lIjoiQ2hpIiwiZmFtaWx5X25hbWUiOiJDaG93IiwibG9jYWxlIjoiZW4iLCJpYXQiOjE1NzI4NTY1ODMsImV4cCI6MTU3Mjg2MDE4M30.MisuC6JnqR-NkCVYMDQiy_PAAeyaETbr-zlHwDM2Xe7lq_YIzm4i0gwgmSJ2jjdyLuzTDW8N83SlvQF8UyCi5drFlMUjtcFYGFpwgi8RykBjZZqR4di6DgTRHNnpjCekDUpG6BTiwReTQxdaMktvx7n3kwVbRuLsk8dKwVpCXGJ66HUg6JaMxP7rwyIQnxNQuiykASM8jEwbZUhu0RWhfqxDzI0OZaTizatmG0HnMTe7qEpM0RvTSUeXdoxIxfKYC41Cmyw6zQc3zWrW3sCpptzfmd0MS60dVh1tw3WcIg7Ci94nzGcYzB4-dCauznL4s4uSafzZz0WYKg4ZGgy1mA"
                }
            }
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }
    }

    @AfterTest
    fun cleanup() {
        client.close()
    }

    @Test
    fun testEcho() = runBlocking {
        val tokenFromServer = client.get<String> {
            url("http://localhost:8080/echotoken")
            contentType(ContentType.Application.Json)
        }
        println(tokenFromServer)
    }

    @Test
    fun testGoogleSignIn() = runBlocking {
        val response = client.get<MmResponse> {
            url("http://localhost:8080/api/protected")
            contentType(ContentType.Application.Json)
        }
        println(response)
    }
}