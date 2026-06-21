package shared.data.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import shared.data.dto.NominatimAddressDto
import shared.data.dto.NominatimReverseGeocodingDto
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData

class ReverseGeocodingMapperTest {

    @Test
    fun should_map_nominatim_response_to_domain() {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val dto = NominatimReverseGeocodingDto(
            displayName = "Bahia Blanca, Partido de Bahia Blanca, Buenos Aires, Argentina",
            address = NominatimAddressDto(
                city = "Bahia Blanca",
                state = "Buenos Aires",
                country = "Argentina",
                countryCode = "ar"
            )
        )

        val result = dto.toDomainModel(coordinates)

        assertEquals(
            LocationData(
                coordinates = coordinates,
                displayName = "Bahia Blanca, Partido de Bahia Blanca, Buenos Aires, Argentina",
                locality = "Bahia Blanca",
                region = "Buenos Aires",
                country = "Argentina",
                countryCode = "ar"
            ),
            result
        )
    }

    @Test
    fun should_fallback_to_address_parts_when_display_name_missing() {
        val coordinates = GeoCoordinates(latitude = -38.7167, longitude = -62.2833)
        val dto = NominatimReverseGeocodingDto(
            address = NominatimAddressDto(
                town = "Punta Alta",
                state = "Buenos Aires",
                country = "Argentina"
            )
        )

        val result = dto.toDomainModel(coordinates)

        assertEquals("Punta Alta, Buenos Aires, Argentina", result.displayName)
        assertEquals("Punta Alta", result.locality)
    }
}
