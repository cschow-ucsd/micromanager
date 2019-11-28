package optaplanner

open class BaseFlexibleEvent(
        name: String,
        open val type: String,
        open val duration: Int,
        longitude: Double,
        latitude: Double
) : BaseEvent(name, longitude, latitude)