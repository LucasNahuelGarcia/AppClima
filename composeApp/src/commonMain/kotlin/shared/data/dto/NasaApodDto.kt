package shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaApodDto(
    val title: String? = null,
    val date: String? = null,
    val explanation: String? = null,
    @SerialName("media_type") val mediaType: String? = null,
    val url: String? = null,
    @SerialName("hdurl") val hdUrl: String? = null,
    val copyright: String? = null
)
