package exposed.dsl

import org.jetbrains.exposed.dao.IntIdTable

object MmSolutionEvents : IntIdTable() {
    val opPID = varchar("op_pid", 50).index()
    val isOpPlanned = bool("op_planned")
    val mmUserId = reference("mm_user", MmUsers)

    // event details
    val name = varchar("name", 50)
    val startTime = integer("startTime")
    val endTime = integer("endTime")
    val longitude = double("longitude")
    val latitude = double("latitude")
}