package exposed.dao

import exposed.dsl.MmSessions
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID

class MmSession(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, MmSession>(MmSessions)

    var sessionBytes by MmSessions.sessionBytes
}