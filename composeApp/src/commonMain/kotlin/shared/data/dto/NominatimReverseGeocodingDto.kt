package shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NominatimReverseGeocodingDto(
    @SerialName("display_name") val displayName: String? = null,
    val address: NominatimAddressDto? = null,
    val error: String? = null
)

@Serializable
data class NominatimAddressDto(
    val city: String? = null,
    val town: String? = null,
    val village: String? = null,
    val municipality: String? = null,
    val suburb: String? = null,
    val neighbourhood: String? = null,
    val county: String? = null,
    val state: String? = null,
    val country: String? = null,
    @SerialName("country_code") val countryCode: String? = null
)
