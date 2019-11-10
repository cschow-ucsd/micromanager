package exposed.dsl

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column

object MmSessions : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("session_id", 100).primaryKey().entityId()
    val sessionBytes = binary("session_bytes", 1200)
}