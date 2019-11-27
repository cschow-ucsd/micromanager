package optaplanner

open class BaseFixedEvent(
        override val name: String,
        override val longitude: Double,
        override val latitude: Double,
        open val startTime: Int,
        open val endTime: Int
) : BaseEvent(name, longitude, latitude)