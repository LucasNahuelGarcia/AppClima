package shared.data.repository

import kotlinx.coroutines.CancellationException
import shared.data.datasource.remote.ApproximateDeviceLocationRemoteDataSource
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.domain.repository.ApproximateDeviceLocationRepository

class ApproximateDeviceLocationRepositoryImpl(
    private val remoteDataSource: ApproximateDeviceLocationRemoteDataSource
) : ApproximateDeviceLocationRepository {

    override suspend fun getApproximateCoordinates(): Result<GeoCoordinates> {
        return try {
            val dto = remoteDataSource.getApproximateLocation()
            if (dto.success && dto.latitude != null && dto.longitude != null) {
                Result.success(
                    GeoCoordinates(
                        latitude = dto.latitude,
                        longitude = dto.longitude
                    )
                )
            } else {
                Result.failure(
                    DomainException(
                        DomainError.LocationFetchFailed(
                            IllegalStateException(dto.message ?: "No se pudo resolver la ubicacion del dispositivo")
                        )
                    )
                )
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(DomainException(DomainError.LocationFetchFailed(e), e))
        }
    }
}
