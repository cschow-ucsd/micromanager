package model

interface BaseFixedEvent: BaseEvent {
    val startTime: Int
    val endTime: Int
}