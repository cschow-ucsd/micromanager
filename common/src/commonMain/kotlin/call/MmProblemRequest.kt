package call

import optaplanner.BaseFixedEvent
import optaplanner.BaseFlexibleEvent
import optaplanner.BaseUserPreferences

data class MmProblemRequest(
        val scheduleName: String,
        val fixedEvents: List<BaseFixedEvent>,
        val toPlanEvents: List<BaseFlexibleEvent>,
        val userPreferences: BaseUserPreferences,
        val currentTime: Int
)