package shared.domain.model

data class LocationData(
    val coordinates: GeoCoordinates,
    val displayName: String,
    val locality: String,
    val region: String,
    val country: String,
    val countryCode: String
)
