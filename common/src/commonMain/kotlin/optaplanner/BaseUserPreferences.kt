package optaplanner

data class BaseUserPreferences (
    val bfStartTime: Int,
    val bfEndTime: Int,
    val lunchStartTime: Int,
    val lunchEndTime: Int,
    val dinnerStartTime: Int,
    val dinnerEndTime: Int,
    val socTime: Int,
    val recTime: Int
)