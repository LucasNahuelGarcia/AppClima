package shared.domain.repository

import shared.domain.model.AstronomyData

interface AstronomyRepository {
    suspend fun getAstronomyData(): Result<AstronomyData>
}
