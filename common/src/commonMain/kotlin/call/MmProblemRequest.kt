package call

import op.BaseFixedEvent
import op.BaseFlexibleEvent

data class MmProblemRequest(
        val fixedEvents: List<BaseFixedEvent>,
        val toPlanEvents: List<BaseFlexibleEvent>
)