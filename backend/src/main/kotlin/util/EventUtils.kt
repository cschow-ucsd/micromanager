package util

import op.BaseFlexibleEvent
import op.PlannedFixedEvent
import op.PlanningFlexibleEvent

fun BaseFlexibleEvent.toPlanning(): PlanningFlexibleEvent {
    return PlanningFlexibleEvent(name, type, longitude, latitude, duration)
}

fun PlanningFlexibleEvent.toFixed(): PlannedFixedEvent {
    val endTime = startTime + duration
    return PlannedFixedEvent(name, longitude, latitude, startTime, endTime)
}
