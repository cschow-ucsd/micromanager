package optaplanner

open class BaseUserPreferences(
        open val bfStartTime: Int, // Starting time for interval of breakfast (any minute from 0 - 1440), for between which times they want to eat breakfast (probs before noon)
        open val bfEndTime: Int, // Ending time for interval of breakfast (same domain as above)
        open val lunchStartTime: Int, // Starting time for interval of lunch (any minute from 0 - 1440)
        open val lunchEndTime: Int, // Ending time for interval of lunch (0 - 1440)
        open val dinnerStartTime: Int, // Starting time for interval of dinner (0 - 1440)
        open val dinnerEndTime: Int, // Ending time for interval of dinner (0 - 1440)
        open val socTime: Int, // Total minutes of social time wanted in a day (0 - 1440)
        open val recTime: Int // Total minutes of recreational time wanted in a day (0 - 1440)
) {
    companion object {
        const val TIME_MAX = 1440
    }
}