package shared.fake

import shared.data.datasource.remote.DeviceLocationRemoteDataSource
import shared.data.dto.IpWhoIsLocationDto

class FakeDeviceLocationRemoteDataSource(
    private val dtoToReturn: IpWhoIsLocationDto? = null,
    private val exceptionToThrow: Exception? = null
) : DeviceLocationRemoteDataSource {
    var calls = 0

    override suspend fun getCurrentLocation(): IpWhoIsLocationDto {
        calls++
        exceptionToThrow?.let { throw it }
        return dtoToReturn
            ?: throw IllegalStateException("Missing IpWhoIsLocationDto")
    }
}
