package shared.data.mapper

import shared.data.dto.NominatimReverseGeocodingDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

fun NominatimReverseGeocodingDto.toDomainModel(coordinates: GeoCoordinates): LocationData {
    val address = address
    val locality = address?.city
        ?: address?.town
        ?: address?.village
        ?: address?.municipality
        ?: address?.suburb
        ?: address?.neighbourhood
        ?: address?.county
        ?: ""

    val fallbackName = listOf(locality, address?.state, address?.country)
        .filter { !it.isNullOrBlank() }
        .joinToString(", ")

    return LocationData(
        coordinates = coordinates,
        displayName = displayName?.takeIf { it.isNotBlank() } ?: fallbackName,
        locality = locality,
        region = address?.state.orEmpty(),
        country = address?.country.orEmpty(),
        countryCode = address?.countryCode.orEmpty()
    )
}
