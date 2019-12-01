package exposed.dao

import exposed.dsl.MmSolutionEvents
import exposed.dsl.MmSolutionSchedules
import exposed.dsl.MmUsers
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID

/**
 * Data access object for the [MmUsers] table.
 * @param [subject] unique user ID corresponding to "subject" in Google accounts.
 */
class MmUser(subject: EntityID<String>) : Entity<String>(subject) {
    companion object : EntityClass<String, MmUser>(MmUsers)

    var email by MmUsers.email
    var refreshToken by MmUsers.refreshToken
    val schedules by MmSolutionSchedule referrersOn MmSolutionSchedules.mmUserId
}