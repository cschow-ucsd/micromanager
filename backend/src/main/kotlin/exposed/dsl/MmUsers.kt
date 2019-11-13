package exposed.dsl

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * Table definition of users in the database.
 */
object MmUsers : IdTable<String>() {
    /**
     * Unique user ID corresponding to "subject" in Google accounts.
     */
    override val id: Column<EntityID<String>> = varchar("subject", 50).primaryKey().entityId()
    val email = varchar("email", 50)
    val refreshToken = text("refresh_token").nullable()
}