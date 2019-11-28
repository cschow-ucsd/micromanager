package optaplanner

open class BaseFixedEvent(
        name: String,
        open val startTime: Int,
        open val endTime: Int,
        longitude: Double,
        latitude: Double
) : BaseEvent(name, longitude, latitude)