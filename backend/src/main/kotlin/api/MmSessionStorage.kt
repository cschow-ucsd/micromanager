package api

import exposed.dao.MmSession
import io.ktor.sessions.SessionStorage
import io.ktor.util.cio.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.writer
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.rowset.serial.SerialBlob

class MmSessionStorage : SessionStorage {
    override suspend fun invalidate(id: String) {
        transaction { MmSession[id].delete() }
    }

    override suspend fun <R> read(id: String, consumer: suspend (ByteReadChannel) -> R): R {
        val blob = transaction { MmSession[id].sessionBytes }
        val bytes = blob.getBytes(1, blob.length().toInt())
        return consumer(ByteReadChannel(bytes))
    }

    override suspend fun write(id: String, provider: suspend (ByteWriteChannel) -> Unit) {
        val bytes = coroutineScope {
            writer(Dispatchers.Unconfined, autoFlush = true) {
                provider(channel)
            }.channel.toByteArray()
        }
        transaction {
            val mmSession = MmSession.findById(id) ?: MmSession.new(id) {}
            mmSession.sessionBytes = SerialBlob(bytes)
        }
    }
}