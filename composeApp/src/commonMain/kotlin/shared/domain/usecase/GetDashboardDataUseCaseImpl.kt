package shared.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import shared.domain.model.DashboardData
import shared.domain.model.DomainError
import shared.domain.model.GeoCoordinates
import shared.domain.model.calculateMoonPhase
import shared.domain.repository.AirQualityRepository
import shared.domain.repository.WeatherRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class GetDashboardDataUseCaseImpl(
    private val weatherRepository: WeatherRepository,
    private val airQualityRepository: AirQualityRepository,
    private val nowProvider: () -> Instant = { Clock.System.now() }
) : GetDashboardDataUseCase {

    override suspend fun invoke(coordinates: GeoCoordinates): Result<DashboardData> = supervisorScope {
        val weatherDeferred = async { weatherRepository.getCurrentWeather(coordinates) }
        val airQualityDeferred = async { airQualityRepository.getAirQuality(coordinates) }

        val weatherResult = weatherDeferred.awaitResult()
        val weatherError = weatherResult.exceptionOrNull()
        if (weatherError != null) {
            return@supervisorScope Result.failure(DomainError.DashboardFetchFailed(weatherError))
        }

        val airQuality = airQualityDeferred.awaitResult().getOrNull()

        Result.success(
            DashboardData(
                weather = weatherResult.getOrThrow(),
                moonPhase = calculateMoonPhase(nowProvider()),
                airQuality = airQuality
            )
        )
    }

    private suspend fun <T> Deferred<Result<T>>.awaitResult(): Result<T> {
        return try {
            await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
