import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.url
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GoogleSignInTest {

    // makes testing easier with serialization
    private data class GoogleTokenHttpResponse(val email: String)

    private lateinit var client: HttpClient
    // insert token for testing
    private val token = ""
    //insert email for testing, e.g. BraBeep@ucsd.edu
    private val email = ""

    @BeforeTest
    fun setup() {
        client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }
    }

    @AfterTest
    fun cleanup() {
        client.close()
    }

    @Test
    fun testGoogleSignInHttp() = runBlocking {
        val response = client.get<GoogleTokenHttpResponse> {
            url("https://oauth2.googleapis.com/tokeninfo?id_token=$token")
        }
        assertEquals(email, response.email)
    }

    @Test
    fun testGoogleSignInVerifier() = runBlocking {
        val verifier = GoogleIdTokenVerifier.Builder(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance()
        ).setAudience(listOf("168037840247-anu4hfobuqucmnpfsflvfbnrgjcp4m9i.apps.googleusercontent.com"))
                .build()

        val token = verifier.verify(token)
        assertEquals(email, token.payload.email)
    }
}