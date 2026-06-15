package shared.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import shared.data.datasource.remote.AstronomyRemoteDataSource
import shared.data.datasource.remote.NasaApodRemoteDataSource
import shared.data.datasource.remote.OpenMeteoWeatherRemoteDataSource
import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.repository.AstronomyRepositoryImpl
import shared.data.repository.WeatherRepositoryImpl
import shared.domain.repository.AstronomyRepository
import shared.domain.repository.WeatherRepository
import shared.domain.usecase.GetDashboardDataUseCase
import shared.domain.usecase.GetDashboardDataUseCaseImpl
import shared.presentation.viewmodel.DashboardViewModel

fun appModule(nasaApiKeyProvider: NasaApiKeyProvider) = module {
    single<HttpClient> { createHttpClient() }
    single { nasaApiKeyProvider }
    single<() -> String> { { get<NasaApiKeyProvider>().getKey() } }
    single<WeatherRemoteDataSource> { OpenMeteoWeatherRemoteDataSource(get()) }
    single<AstronomyRemoteDataSource> { NasaApodRemoteDataSource(get(), get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<AstronomyRepository> { AstronomyRepositoryImpl(get()) }
    single<GetDashboardDataUseCase> { GetDashboardDataUseCaseImpl(get(), get()) }
    single { DashboardViewModel(get()) }
}