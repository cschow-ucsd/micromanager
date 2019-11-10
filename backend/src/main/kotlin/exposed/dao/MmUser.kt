package exposed.dao

import exposed.dsl.MmUsers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID

class MmUser(subject: EntityID<String>) : Entity<String>(subject) {
    companion object : EntityClass<String, MmUser>(MmUsers)

    var email by MmUsers.email
    var refreshToken by MmUsers.refreshToken
}