package api

data class TravelTimeRequest(
        val locations: List<Location>,
        val departure_searches: List<DepartureSearch>,
        val arrival_searches: List<ArrivalSearch>
) {
    data class ArrivalSearch(
            val arrival_location_id: String,
            val arrival_time: String,
            val departure_location_ids: List<String>,
            val id: String,
            val properties: List<String>,
            val transportation: Transportation,
            val travel_time: Int
    ) {
        data class Transportation(
                val type: String
        )
    }

    data class DepartureSearch(
            val arrival_location_ids: List<String>,
            val departure_location_id: String,
            val departure_time: String,
            val id: String,
            val properties: List<String>,
            val range: Range,
            val transportation: Transportation,
            val travel_time: Int
    ) {
        data class Range(
                val enabled: Boolean,
                val max_results: Int,
                val width: Int
        )

        data class Transportation(
                val type: String
        )
    }

    data class Location(
            val id: String,
            val coords: Coords
    ) {
        data class Coords(
                val lng: Double,
                val lat: Double
        )
    }
}