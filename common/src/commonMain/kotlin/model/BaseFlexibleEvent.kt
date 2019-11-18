package model

interface BaseFlexibleEvent: BaseEvent {
    var type: String
    var duration: Int
}