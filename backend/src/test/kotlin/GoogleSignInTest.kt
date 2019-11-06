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
import util.CLIENT_EMAIL_TEST
import util.CLIENT_OAUTH_TOKEN_TEST
import util.testMmDotenv
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GoogleSignInTest {

    // makes testing easier with serialization
    private data class GoogleTokenHttpResponse(val email: String)

    private lateinit var client: HttpClient
    // insert token
    private val token = testMmDotenv.CLIENT_OAUTH_TOKEN_TEST
    // insert email, e.g. BraBeep@ucsd.edu
    private val email = testMmDotenv.CLIENT_EMAIL_TEST

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
        ).setAudience(listOf(testMmDotenv.BACKEND_OAUTH_CLIENT_ID))
                .build()

        val token = verifier.verify(token)
        assertEquals(email, token.payload.email)
    }
}