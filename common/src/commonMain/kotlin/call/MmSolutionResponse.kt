package call

import events.BaseFixedEvent

data class MmSolutionResponse(
        val fixedEvents: List<BaseFixedEvent>,
        val plannedEvents: List<BaseFixedEvent>
)