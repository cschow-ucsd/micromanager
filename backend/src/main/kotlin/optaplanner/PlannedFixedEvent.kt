package optaplanner

class PlannedFixedEvent(
        event: PlanningFlexibleEvent
) : BaseEvent by event, BaseFixedEvent {
    override val startTime: Int = event.startTime
    override val endTime: Int = event.startTime + event.duration
}