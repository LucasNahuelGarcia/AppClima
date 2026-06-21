package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import shared.data.dto.IpWhoIsLocationDto

class IpWhoIsDeviceLocationRemoteDataSource(
    private val client: HttpClient
) : DeviceLocationRemoteDataSource {

    override suspend fun getCurrentLocation(): IpWhoIsLocationDto {
        return client.get("https://ipwho.is/").body()
    }
}
