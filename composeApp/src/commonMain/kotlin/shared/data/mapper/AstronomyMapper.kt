package shared.data.mapper

import shared.data.dto.NasaApodDto
import shared.domain.model.AstronomyData

fun NasaApodDto.toDomainModel(): AstronomyData {
    return AstronomyData(
        title = title.orEmpty(),
        date = date.orEmpty(),
        explanation = explanation.orEmpty(),
        mediaType = mediaType.orEmpty(),
        url = url.orEmpty(),
        hdUrl = hdUrl
    )
}
