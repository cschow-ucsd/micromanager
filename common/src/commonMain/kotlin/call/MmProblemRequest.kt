package call

import optaplanner.BaseFixedEvent
import optaplanner.BaseFlexibleEvent

data class MmProblemRequest(
        val fixedEvents: List<BaseFixedEvent>,
        val toPlanEvents: List<BaseFlexibleEvent>
)