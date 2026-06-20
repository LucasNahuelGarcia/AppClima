package shared.di

import org.koin.core.qualifier.named
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

fun appModule(apiKeyProvider: NasaApiKeyProvider) = module {
    single(named("weather")) { createWeatherHttpClient() }
    single(named("nasa")) { createNasaHttpClient() }
    single<NasaApiKeyProvider> { apiKeyProvider }

    single<WeatherRemoteDataSource> {
        OpenMeteoWeatherRemoteDataSource(get(named("weather")))
    }

    single<AstronomyRemoteDataSource> {
        NasaApodRemoteDataSource(get(named("nasa"))) { get<NasaApiKeyProvider>().getKey() }
    }

    single<WeatherRepository> {
        WeatherRepositoryImpl(get())
    }

    single<AstronomyRepository> {
        AstronomyRepositoryImpl(get())
    }

    single<GetDashboardDataUseCase> {
        GetDashboardDataUseCaseImpl(get(), get())
    }

    single {
        DashboardViewModel(get())
    }
}



