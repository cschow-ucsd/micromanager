package ucsd.ieeeqp.fa19

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ucsd.ieeeqp.fa19.service.MmService

class MmServiceTest {
    @Test
    fun testRepeatedApiCalls() = runBlocking {
        // insert server auth token
        val serverAuthToken = ""

        val service = MmService(serverAuthToken)
        val responses = mutableListOf<String>()
        repeat(5) {
            delay(3000)
            responses.add(service.getProtectedAsync().await())
        }

        assert(responses.all { it == responses[0] })
    }
}