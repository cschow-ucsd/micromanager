package api

data class TravelTimeResponse(
        val results: List<Result>
) {
    data class Result(
            val locations: List<Location>,
            val search_id: String,
            val unreachable: List<String>
    ) {
        data class Location(
                val id: String,
                val properties: List<Property>
        ) {
            data class Property(
                    val travel_time: Int
            )
        }
    }
}