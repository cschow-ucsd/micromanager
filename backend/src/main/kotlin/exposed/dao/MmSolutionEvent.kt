package exposed.dao

import exposed.dsl.MmSolutionEvents
import optaplanner.BaseFixedEvent
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class MmSolutionEvent(id: EntityID<Int>) : IntEntity(id), BaseFixedEvent {
    companion object : IntEntityClass<MmSolutionEvent>(MmSolutionEvents)

    var opPID by MmSolutionEvents.opPID
    var isOpPlanned by MmSolutionEvents.isOpPlanned
    var mmUser by MmUser referencedOn MmSolutionEvents.mmUser

    // event details
    override var name: String by MmSolutionEvents.name
    override var startTime: Int by MmSolutionEvents.startTime
    override var endTime: Int by MmSolutionEvents.endTime
    override var longitude: Double by MmSolutionEvents.longitude
    override var latitude: Double by MmSolutionEvents.latitude
}