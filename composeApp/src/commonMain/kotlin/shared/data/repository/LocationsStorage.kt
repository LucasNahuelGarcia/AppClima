package shared.data.repository

import kotlinx.serialization.Serializable
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

interface LocationsStorage {
    fun readSavedLocations(): List<LocationData>
    fun writeSavedLocations(locations: List<LocationData>)
}

class NoOpLocationsStorage : LocationsStorage {
    override fun readSavedLocations(): List<LocationData> {
        return emptyList()
    }

    override fun writeSavedLocations(locations: List<LocationData>) = Unit
}

@Serializable
internal data class SavedLocationsDto(
    val locations: List<SavedLocationDto> = emptyList()
)

@Serializable
internal data class SavedLocationDto(
    val latitude: Double,
    val longitude: Double,
    val displayName: String,
    val locality: String,
    val region: String,
    val country: String,
    val countryCode: String
)

internal fun LocationData.toSavedLocationDto(): SavedLocationDto {
    return SavedLocationDto(
        latitude = coordinates.latitude,
        longitude = coordinates.longitude,
        displayName = displayName,
        locality = locality,
        region = region,
        country = country,
        countryCode = countryCode
    )
}

internal fun SavedLocationDto.toDomainModel(): LocationData {
    return LocationData(
        coordinates = GeoCoordinates(
            latitude = latitude,
            longitude = longitude
        ),
        displayName = displayName,
        locality = locality,
        region = region,
        country = country,
        countryCode = countryCode
    )
}
