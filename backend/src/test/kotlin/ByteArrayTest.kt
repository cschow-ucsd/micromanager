import org.junit.Test
import javax.sql.rowset.serial.SerialBlob
import kotlin.test.assertEquals

class ByteArrayTest {
    @Test
    fun convertToFromStringTest() {
        val original = "yeet yeet"
        val byteArray = original.toByteArray()
        val blob = SerialBlob(byteArray)
        assertEquals(original, blob.toString())
    }
}