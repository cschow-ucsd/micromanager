package call

import optaplanner.BaseFixedEvent

data class MmSolutionResponse(
        val fixedEvents: List<BaseFixedEvent>,
        val plannedEvents: List<BaseFixedEvent>
)