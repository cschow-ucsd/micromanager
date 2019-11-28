import api.HelloWorld
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class MainTest {
    @Test
    fun addition() {
        val calculation = 1 + 1
        val result = 2
        assertEquals(calculation, result)
    }

    @Test
    fun testSomething() = runBlocking<Unit> {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = GsonSerializer {
                    serializeNulls()
                    disableHtmlEscaping()
                }
            }
        }
        val testBody = HelloWorld("Message")
        val response = client.post<HelloWorld>() {
            url("http://localhost:8080/yeet")
            contentType(ContentType.Application.Json)
            body = testBody
        }
        client.close()
        assertEquals(testBody, response)
    }
}