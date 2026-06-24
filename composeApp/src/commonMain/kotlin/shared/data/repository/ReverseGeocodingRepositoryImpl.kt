package shared.data.repository

import kotlinx.coroutines.CancellationException
import shared.data.datasource.remote.ReverseGeocodingRemoteDataSource
import shared.data.mapper.toDomainModel
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.domain.repository.ReverseGeocodingRepository

class ReverseGeocodingRepositoryImpl(
    private val remoteDataSource: ReverseGeocodingRemoteDataSource
) : ReverseGeocodingRepository {

    override suspend fun getLocation(coordinates: GeoCoordinates): Result<LocationData> {
        return try {
            val dto = remoteDataSource.getLocation(coordinates)
            if (dto.error == "Unable to geocode") {
                return Result.failure(DomainException(DomainError.UnableToGeocode))
            }

            Result.success(dto.toDomainModel(coordinates))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(DomainException(DomainError.ReverseGeocodingFailed(e), e))
        }
    }
}