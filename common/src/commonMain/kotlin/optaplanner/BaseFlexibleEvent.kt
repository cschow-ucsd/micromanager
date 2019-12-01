package optaplanner

open class BaseFlexibleEvent(
        name: String,
        open val type: String,
        open val duration: Int,
        longitude: Double,
        latitude: Double
) : BaseEvent(name, longitude, latitude)
{
    companion object {
        const val BREAKFAST = "Breakfast"
        const val LUNCH = "Lunch"
        const val DINNER = "Dinner"
        const val SOCIAL = "Social"
        const val REC = "Recreational"
        const val HW = "Homework"
    }
}
