package shared.domain.usecase

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import shared.domain.model.DashboardData
import shared.domain.model.DomainError
import shared.domain.model.GeoCoordinates
import shared.domain.repository.AstronomyRepository
import shared.domain.repository.WeatherRepository

class GetDashboardDataUseCaseImpl(
    private val weatherRepository: WeatherRepository,
    private val astronomyRepository: AstronomyRepository
) : GetDashboardDataUseCase {

    override suspend fun invoke(coordinates: GeoCoordinates): Result<DashboardData> = supervisorScope {
        val weatherDeferred = async {
            try {
                weatherRepository.getCurrentWeather(coordinates)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }
        val astronomyDeferred = async {
            try {
                astronomyRepository.getAstronomyData()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Result.failure(e)
            }
        }

        val weatherResult = weatherDeferred.await()
        val astronomyResult = astronomyDeferred.await()

        val weatherError = weatherResult.exceptionOrNull()
        if (weatherError != null) {
            return@supervisorScope Result.failure(DomainError.DashboardFetchFailed(weatherError))
        }
        val astronomyError = astronomyResult.exceptionOrNull()
        if (astronomyError != null) {
            return@supervisorScope Result.failure(DomainError.DashboardFetchFailed(astronomyError))
        }

        Result.success(
            DashboardData(
                weather = weatherResult.getOrThrow(),
                astronomy = astronomyResult.getOrThrow()
            )
        )
    }
}
