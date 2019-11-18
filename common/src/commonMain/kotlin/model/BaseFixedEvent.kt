package model

interface BaseFixedEvent : BaseEvent {
    val endTime: Int
    val startTime: Int
}