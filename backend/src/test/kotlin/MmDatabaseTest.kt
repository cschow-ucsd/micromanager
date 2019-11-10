import exposed.MmDatabase
import exposed.dao.MmUser
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MmDatabaseTest {
    @BeforeTest
    fun setup() {
        MmDatabase.setup()
    }

    @Test
    fun insertDeleteUserTest() {
        val testUserName = "test user"
        transaction {
            val user1 = MmUser.findById(testUserName)
            assertNull(user1)
        }
        transaction {
            val user2 = MmUser.new(testUserName) {
                email = "testuser@testuser.com"
                refreshToken = "blah blah blah"
            }
            assertNotNull(MmUser[testUserName].also { println(it.refreshToken) })
            user2.delete()
        }
        transaction {
            val user3 = MmUser.findById(testUserName)
            assertNull(user3)
        }
    }
}