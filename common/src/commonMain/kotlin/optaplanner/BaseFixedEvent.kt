package optaplanner

open class BaseFixedEvent(
        override val name: String,
        open val startTime: Int,
        open val endTime: Int,
        override val longitude: Double,
        override val latitude: Double
) : BaseEvent(name, longitude, latitude)