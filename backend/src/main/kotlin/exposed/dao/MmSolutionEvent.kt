package exposed.dao

import exposed.dsl.MmSolutionEvents
import optaplanner.BaseFixedEvent
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class MmSolutionEvent(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MmSolutionEvent>(MmSolutionEvents)

    var opPID by MmSolutionEvents.opPID
    var isOpPlanned by MmSolutionEvents.isOpPlanned
    var mmUser by MmUser referencedOn MmSolutionEvents.mmUserId

    // event details
    var name: String by MmSolutionEvents.name
    var startTime: Int by MmSolutionEvents.startTime
    var endTime: Int by MmSolutionEvents.endTime
    var longitude: Double by MmSolutionEvents.longitude
    var latitude: Double by MmSolutionEvents.latitude
}

fun MmSolutionEvent.toBaseFixed() = BaseFixedEvent(name, longitude, latitude, startTime, endTime)