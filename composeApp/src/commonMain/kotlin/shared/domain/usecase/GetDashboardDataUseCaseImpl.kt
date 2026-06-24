package shared.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import shared.domain.model.DashboardData
import shared.domain.model.DomainError
import shared.domain.model.DomainException
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
        val weatherDeferred = async { weatherRepository.getWeather(coordinates) }
        val airQualityDeferred = async { airQualityRepository.getAirQuality(coordinates) }

        val weatherResult = try {
            weatherDeferred.awaitAndPropagateException()
        } catch (e: Exception) {
            return@supervisorScope Result.failure(DomainException(DomainError.DashboardFetchFailed(e), e))
        }

        val airQualityResult = try {
            airQualityDeferred.awaitAndPropagateException()
        } catch (e: Exception) {
            return@supervisorScope Result.failure(DomainException(DomainError.DashboardFetchFailed(e), e))
        }

        if (weatherResult.isFailure) {
            val cause = weatherResult.exceptionOrNull()
            val domainError = (cause as? DomainException)?.domainError ?: DomainError.DashboardFetchFailed(cause)
            return@supervisorScope Result.failure(DomainException(domainError, cause))
        }

        if (airQualityResult.isFailure) {
            val cause = airQualityResult.exceptionOrNull()
            val domainError = (cause as? DomainException)?.domainError ?: DomainError.DashboardFetchFailed(cause)
            return@supervisorScope Result.failure(DomainException(domainError, cause))
        }

        Result.success(
            DashboardData(
                weather = weatherResult.getOrThrow(),
                moonPhase = calculateMoonPhase(nowProvider()),
                airQuality = airQualityResult.getOrThrow()
            )
        )
    }

    private suspend fun <T> Deferred<Result<T>>.awaitAndPropagateException(): Result<T> {
        return try {
            await()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw e
        }
    }
}