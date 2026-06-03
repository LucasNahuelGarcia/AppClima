package shared.data.repository

import kotlinx.coroutines.CancellationException
import shared.data.datasource.remote.AstronomyRemoteDataSource
import shared.data.mapper.toDomainModel
import shared.domain.model.AstronomyData
import shared.domain.model.DomainError
import shared.domain.repository.AstronomyRepository

class AstronomyRepositoryImpl(
    private val remoteDataSource: AstronomyRemoteDataSource
) : AstronomyRepository {

    override suspend fun getAstronomyData(): Result<AstronomyData> {
        return try {
            val dto = remoteDataSource.getAstronomyData()
            Result.success(dto.toDomainModel())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val error = if (e is DomainError) e else DomainError.DashboardFetchFailed(e)
            Result.failure(error)
        }
    }
}
