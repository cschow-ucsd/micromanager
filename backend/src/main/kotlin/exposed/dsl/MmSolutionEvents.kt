package exposed.dsl

import org.jetbrains.exposed.dao.IntIdTable

object MmSolutionEvents : IntIdTable() {
    val schedule = reference("schedule", MmSolutionSchedules)
    val isOpPlanned = bool("op_planned")

    // event details
    val name = varchar("name", 50)
    val startTime = integer("startTime")
    val endTime = integer("endTime")
    val longitude = double("longitude")
    val latitude = double("latitude")
}