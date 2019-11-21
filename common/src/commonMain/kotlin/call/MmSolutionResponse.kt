package call

import op.BaseFixedEvent

data class MmSolutionResponse(
        val fixedEvents: List<BaseFixedEvent>,
        val plannedEvents: List<BaseFixedEvent>
)