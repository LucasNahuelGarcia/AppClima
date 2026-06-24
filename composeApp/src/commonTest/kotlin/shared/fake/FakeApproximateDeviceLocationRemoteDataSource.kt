package shared.fake

import shared.data.datasource.remote.ApproximateDeviceLocationRemoteDataSource
import shared.data.dto.IpWhoIsLocationDto

class FakeApproximateDeviceLocationRemoteDataSource(
    private val dtoToReturn: IpWhoIsLocationDto? = null,
    private val exceptionToThrow: Exception? = null
) : ApproximateDeviceLocationRemoteDataSource {
    var calls = 0

    override suspend fun getApproximateLocation(): IpWhoIsLocationDto {
        calls++
        exceptionToThrow?.let { throw it }
        return dtoToReturn
            ?: throw IllegalStateException("Missing IpWhoIsLocationDto")
    }
}
