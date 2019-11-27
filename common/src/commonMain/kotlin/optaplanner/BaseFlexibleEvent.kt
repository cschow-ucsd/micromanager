package optaplanner

open class BaseFlexibleEvent(
        override val name: String,
        override val longitude: Double,
        override val latitude: Double,
        open val type: String,
        open val duration: Int
) : BaseEvent(name, longitude, latitude)