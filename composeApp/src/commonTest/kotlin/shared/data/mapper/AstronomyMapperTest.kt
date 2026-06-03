package shared.data.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import shared.data.dto.NasaApodDto

class AstronomyMapperTest {

    @Test
    fun should_map_nasa_apod_with_nulls_to_domain_defaults() {
        val dto = NasaApodDto(
            title = null,
            date = "2026-06-02",
            explanation = null,
            mediaType = "image",
            url = null,
            hdUrl = null
        )

        val result = dto.toDomainModel()

        assertEquals("", result.title)
        assertEquals("2026-06-02", result.date)
        assertEquals("", result.explanation)
        assertEquals("image", result.mediaType)
        assertEquals("", result.url)
        assertNull(result.hdUrl)
    }
}
