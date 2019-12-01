package api

data class TravelTimeRequest(
    val arrival_searches: List<ArrivalSearche>,
    val departure_searches: List<DepartureSearche>,
    val locations: List<Location>
) {
    data class ArrivalSearche(
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

    data class DepartureSearche(
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
        val coords: Coords,
        val id: String
    ) {
        data class Coords(
            val lat: Double,
            val lng: Double
        )
    }
}