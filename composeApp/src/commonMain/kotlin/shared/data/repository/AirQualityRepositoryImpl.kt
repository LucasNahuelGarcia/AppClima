package shared.data.repository

import kotlinx.coroutines.CancellationException
import shared.data.datasource.remote.AirQualityRemoteDataSource
import shared.data.mapper.toDomainModel
import shared.domain.model.AirQualityData
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.domain.repository.AirQualityRepository

class AirQualityRepositoryImpl(
    private val remoteDataSource: AirQualityRemoteDataSource
) : AirQualityRepository {

    override suspend fun getAirQuality(coordinates: GeoCoordinates): Result<AirQualityData> {
        return try {
            val dto = remoteDataSource.getAirQuality(coordinates)
            Result.success(dto.toDomainModel())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(DomainException(DomainError.AirQualityFetchFailed(e), e))
        }
    }
}