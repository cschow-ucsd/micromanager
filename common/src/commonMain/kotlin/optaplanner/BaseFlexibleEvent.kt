package optaplanner

open class BaseFlexibleEvent(
        override val name: String,
        open val type: String,
        open val duration: Int,
        override val longitude: Double,
        override val latitude: Double
) : BaseEvent(name, longitude, latitude)