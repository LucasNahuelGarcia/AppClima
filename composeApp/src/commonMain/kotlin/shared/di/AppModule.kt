package shared.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import shared.data.datasource.remote.AstronomyRemoteDataSource
import shared.data.datasource.remote.DeviceLocationRemoteDataSource
import shared.data.datasource.remote.IpWhoIsDeviceLocationRemoteDataSource
import shared.data.datasource.remote.NasaApodRemoteDataSource
import shared.data.datasource.remote.NominatimReverseGeocodingRemoteDataSource
import shared.data.datasource.remote.OpenMeteoWeatherRemoteDataSource
import shared.data.datasource.remote.ReverseGeocodingRemoteDataSource
import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.repository.AstronomyRepositoryImpl
import shared.data.repository.DeviceLocationRepositoryImpl
import shared.data.repository.ReverseGeocodingRepositoryImpl
import shared.data.repository.WeatherRepositoryImpl
import shared.domain.repository.AstronomyRepository
import shared.domain.repository.DeviceLocationRepository
import shared.domain.repository.ReverseGeocodingRepository
import shared.domain.repository.WeatherRepository
import shared.domain.usecase.GetDeviceLocationUseCase
import shared.domain.usecase.GetDeviceLocationUseCaseImpl
import shared.domain.usecase.GetDashboardDataUseCase
import shared.domain.usecase.GetDashboardDataUseCaseImpl
import shared.domain.usecase.GetReverseGeocodingUseCase
import shared.domain.usecase.GetReverseGeocodingUseCaseImpl
import shared.presentation.viewmodel.DashboardViewModel

fun appModule(nasaApiKeyProvider: NasaApiKeyProvider) = module {
    single<HttpClient> { createHttpClient() }
    single { nasaApiKeyProvider }
    single<() -> String> { { get<NasaApiKeyProvider>().getKey() } }
    single<WeatherRemoteDataSource> { OpenMeteoWeatherRemoteDataSource(get()) }
    single<AstronomyRemoteDataSource> { NasaApodRemoteDataSource(get(), get()) }
    single<DeviceLocationRemoteDataSource> { IpWhoIsDeviceLocationRemoteDataSource(get()) }
    single<ReverseGeocodingRemoteDataSource> { NominatimReverseGeocodingRemoteDataSource(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<AstronomyRepository> { AstronomyRepositoryImpl(get()) }
    single<DeviceLocationRepository> { DeviceLocationRepositoryImpl(get()) }
    single<ReverseGeocodingRepository> { ReverseGeocodingRepositoryImpl(get()) }
    single<GetDeviceLocationUseCase> { GetDeviceLocationUseCaseImpl(get()) }
    single<GetDashboardDataUseCase> { GetDashboardDataUseCaseImpl(get(), get()) }
    single<GetReverseGeocodingUseCase> { GetReverseGeocodingUseCaseImpl(get()) }
    single { DashboardViewModel(get(), get()) }
}
