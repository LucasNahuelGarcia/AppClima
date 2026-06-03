package shared.data.repository

import kotlinx.coroutines.CancellationException
import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.mapper.toDomainModel
import shared.domain.model.DomainError
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import shared.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource
) : WeatherRepository {

    override suspend fun getCurrentWeather(coordinates: GeoCoordinates): Result<WeatherData> {
        return try {
            val dto = remoteDataSource.getCurrentWeather(coordinates)
            Result.success(dto.toDomainModel())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val error = if (e is DomainError) e else DomainError.DashboardFetchFailed(e)
            Result.failure(error)
        }
    }
}
