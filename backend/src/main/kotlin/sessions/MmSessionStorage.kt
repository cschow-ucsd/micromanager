package sessions

import exposed.dao.MmSession
import io.ktor.sessions.SessionStorage
import io.ktor.util.cio.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.ByteWriteChannel
import kotlinx.coroutines.io.writer
import org.jetbrains.exposed.sql.transactions.transaction

class MmSessionStorage : SessionStorage {
    override suspend fun <R> read(id: String, consumer: suspend (ByteReadChannel) -> R): R {
        val mmSession = transaction { MmSession[id] }
        return consumer(ByteReadChannel(mmSession.sessionBytes))
    }

    override suspend fun write(id: String, provider: suspend (ByteWriteChannel) -> Unit) {
        val sessionBytes = coroutineScope {
            writer(Dispatchers.Unconfined, autoFlush = true) {
                provider(channel)
            }.channel.toByteArray()
        }
        transaction { MmSession[id].sessionBytes = sessionBytes }
    }

    override suspend fun invalidate(id: String) {
        transaction { MmSession[id].delete() }
    }
}