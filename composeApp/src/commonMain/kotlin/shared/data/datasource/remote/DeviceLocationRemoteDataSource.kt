package shared.data.datasource.remote

import shared.data.dto.IpWhoIsLocationDto

interface DeviceLocationRemoteDataSource {
    suspend fun getCurrentLocation(): IpWhoIsLocationDto
}
