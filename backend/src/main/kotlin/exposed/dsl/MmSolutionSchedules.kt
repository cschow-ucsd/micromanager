package exposed.dsl

import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column

object MmSolutionSchedules : IdTable<String>() {
    override val id: Column<EntityID<String>> = varchar("op_pid", 50).primaryKey().entityId()
    val scheduleName = varchar("schedule_name", 50)
    val mmUserId = reference("mm_user", MmUsers)
}