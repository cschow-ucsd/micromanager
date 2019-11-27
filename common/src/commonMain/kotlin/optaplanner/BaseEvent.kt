package optaplanner

abstract class BaseEvent(
        open val name: String,
        open val longitude: Double,
        open val latitude: Double
)