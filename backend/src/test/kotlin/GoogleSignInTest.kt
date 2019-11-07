import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import util.CLIENT_EMAIL_TEST
import util.CLIENT_SERVER_AUTH_TOKEN_TEST
import util.testMmDotenv
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GoogleSignInTest {

    // makes testing easier with serialization
    private data class GoogleTokenHttpResponse(val email: String)

    private lateinit var client: HttpClient
    private val token = testMmDotenv.CLIENT_SERVER_AUTH_TOKEN_TEST
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
    fun testGoogleSignInVerifier() {
        val request = GoogleAuthorizationCodeTokenRequest(
                NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                "https://oauth2.googleapis.com/token",
                testMmDotenv.BACKEND_OAUTH_CLIENT_ID,
                testMmDotenv.BACKEND_OAUTH_CLIENT_SECRET,
                testMmDotenv.CLIENT_SERVER_AUTH_TOKEN_TEST,
                "" // no redirect URL needed
        )
        val tokenResponse: GoogleTokenResponse = request.execute()
        val idToken: GoogleIdToken = tokenResponse.parseIdToken()
        assertEquals(testMmDotenv.CLIENT_EMAIL_TEST, idToken.payload.email)
    }
}