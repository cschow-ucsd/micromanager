package call

import events.BaseFixedEvent
import events.BaseFlexibleEvent

data class MmProblemRequest(
        val fixedEvents: List<BaseFixedEvent>,
        val toPlanEvents: List<BaseFlexibleEvent>
)