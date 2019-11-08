package ucsd.ieeeqp.fa19

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ucsd.ieeeqp.fa19.service.MmService

class MmServiceTest {
    @Test
    fun testRepeatedApiCalls() = runBlocking {
        // insert server auth token
        val serverAuthToken = "4/tAH3kIr0sDTdZZ5LAJMLweGjPArQK0bwl_0qDBnzz7jaHY2ZN2kitob12vkIhqN1eh66Q2GhE3tHX756_1aoLVs"

        val service = MmService(serverAuthToken)
        val responses = mutableListOf<String>()
        repeat(5) {
            delay(3000)
            val response = service.getProtectedAsync().await()
            println(response)
            responses.add(response)
        }

        assert(responses.all { it == responses[0] })
    }
}