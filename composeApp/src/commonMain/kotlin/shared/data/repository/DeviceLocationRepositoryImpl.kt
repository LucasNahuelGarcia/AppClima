package shared.data.repository

import kotlinx.coroutines.CancellationException
import shared.data.datasource.remote.DeviceLocationRemoteDataSource
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.domain.repository.DeviceLocationRepository

class DeviceLocationRepositoryImpl(
    private val remoteDataSource: DeviceLocationRemoteDataSource
) : DeviceLocationRepository {

    override suspend fun getCurrentCoordinates(): Result<GeoCoordinates> {
        return try {
            val dto = remoteDataSource.getCurrentLocation()
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