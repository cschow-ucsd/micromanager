package optaplanner

data class BaseUserPreferences (
    val bfStartTime: Int, // Starting time for interval of breakfast (any minute from 0 - 1440), for between which times they want to eat breakfast (probs before noon)
    val bfEndTime: Int, // Ending time for interval of breakfast (same domain as above)
    val lunchStartTime: Int, // Starting time for interval of lunch (any minute from 0 - 1440)
    val lunchEndTime: Int, // Ending time for interval of lunch (0 - 1440)
    val dinnerStartTime: Int, // Starting time for interval of dinner (0 - 1440)
    val dinnerEndTime: Int, // Ending time for interval of dinner (0 - 1440)
    val hwStartTime: Int,
    val hwEndTime: Int,  //Between what time do they like to do their homework
    val socTime: Int, // Total minutes of social time wanted in a day (0 - 1440)
    val recTime: Int // Total minutes of recreational time wanted in a day (0 - 1440)
)