package shared.fake

import shared.domain.model.AstronomyData
import shared.domain.repository.AstronomyRepository

class FakeAstronomyRepository(
    private val result: Result<AstronomyData>
) : AstronomyRepository {
    var calls = 0

    override suspend fun getAstronomyData(): Result<AstronomyData> {
        calls++
        return result
    }
}
