package shared.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.mapper.toDomainModel
import shared.domain.model.DomainError
import shared.domain.model.DomainException
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import shared.domain.repository.WeatherRepository

class WeatherRepositoryImpl(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val nowProvider: () -> Instant = { Clock.System.now() }
) : WeatherRepository {

    override suspend fun getWeather(coordinates: GeoCoordinates): Result<WeatherData> {
        return try {
            val dto = remoteDataSource.getWeather(coordinates)
            Result.success(dto.toDomainModel(nowProvider))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(DomainException(DomainError.DashboardFetchFailed(e), e))
        }
    }
}