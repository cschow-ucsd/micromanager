import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
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
        val response = client.get<String>("http://localhost:8080/what")
        println(response)
    }
}