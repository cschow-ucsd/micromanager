package call

data class MmSolveStatus(
        val scheduleName: String,
        val pid: String,
        val done: Boolean
)