package exposed.dao

import exposed.dsl.MmSolutionEvents
import exposed.dsl.MmSolutionSchedules
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID

class MmSolutionSchedule(opPid: EntityID<String>) : Entity<String>(opPid) {
    companion object : EntityClass<String, MmSolutionSchedule>(MmSolutionSchedules)

    var mmUser by MmUser referencedOn MmSolutionSchedules.mmUserId
    var scheduleName by MmSolutionSchedules.scheduleName
    val events by MmSolutionEvent referrersOn MmSolutionEvents.schedule
}

