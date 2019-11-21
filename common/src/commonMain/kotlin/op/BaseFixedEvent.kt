package op

interface BaseFixedEvent : BaseEvent {
    val endTime: Int
    val startTime: Int
}