package shared.data.datasource.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import shared.data.dto.IpWhoIsLocationDto

class IpWhoIsApproximateDeviceLocationRemoteDataSource(
    private val client: HttpClient
) : ApproximateDeviceLocationRemoteDataSource {

    override suspend fun getApproximateLocation(): IpWhoIsLocationDto {
        return client.get("https://ipwho.is/").body()
    }
}
