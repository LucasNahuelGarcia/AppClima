package shared.data.datasource.remote

import shared.data.dto.IpWhoIsLocationDto

interface ApproximateDeviceLocationRemoteDataSource {
    suspend fun getApproximateLocation(): IpWhoIsLocationDto
}
