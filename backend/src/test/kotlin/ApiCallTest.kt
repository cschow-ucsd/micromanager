import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.providers.basic
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.runBlocking
import util.CLIENT_SERVER_AUTH_TOKEN_TEST
import util.testMmDotenv
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
                    password = testMmDotenv.CLIENT_SERVER_AUTH_TOKEN_TEST
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
    fun testProtectedApi() = runBlocking {
        val response = client.get<HttpResponse> {
            header("MICROMANAGER_SESSION", null)
            url("http://localhost:8080/api/protected")
            contentType(ContentType.Application.Json)
        }
        println("Headers: ${response.headers}")
        println(response.headers["MICROMANAGER_SESSION"])
        println("Text: ${response.readText()}")
        val deferred: Deferred<String>
    }
}