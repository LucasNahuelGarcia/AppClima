package shared.data.datasource.remote

import shared.data.dto.NasaApodDto

interface AstronomyRemoteDataSource {
    suspend fun getAstronomyData(): NasaApodDto
}
