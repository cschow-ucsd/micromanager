package model

interface BaseFlexibleEvent : BaseEvent {
    val type: String
    val duration: Int
}