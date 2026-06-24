package shared.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import shared.data.datasource.remote.AirQualityRemoteDataSource
import shared.data.datasource.remote.DeviceLocationRemoteDataSource
import shared.data.datasource.remote.IpWhoIsDeviceLocationRemoteDataSource
import shared.data.datasource.remote.NominatimReverseGeocodingRemoteDataSource
import shared.data.datasource.remote.OpenMeteoAirQualityRemoteDataSource
import shared.data.datasource.remote.OpenMeteoWeatherRemoteDataSource
import shared.data.datasource.remote.ReverseGeocodingRemoteDataSource
import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.repository.AirQualityRepositoryImpl
import shared.data.repository.InMemoryLocationsProvider
import shared.data.repository.DeviceLocationRepositoryImpl
import shared.data.repository.LocationsStorage
import shared.data.repository.ReverseGeocodingRepositoryImpl
import shared.data.repository.WeatherRepositoryImpl
import shared.data.repository.createLocationsStorage
import shared.domain.repository.AirQualityRepository
import shared.domain.repository.DeviceLocationRepository
import shared.domain.repository.LocationsProvider
import shared.domain.repository.ReverseGeocodingRepository
import shared.domain.repository.WeatherRepository
import shared.domain.usecase.GetDeviceLocationUseCase
import shared.domain.usecase.GetDeviceLocationUseCaseImpl
import shared.domain.usecase.GetDashboardDataUseCase
import shared.domain.usecase.GetDashboardDataUseCaseImpl
import shared.domain.usecase.GetReverseGeocodingUseCase
import shared.domain.usecase.GetReverseGeocodingUseCaseImpl
import shared.presentation.viewmodel.AppViewModel
import shared.presentation.viewmodel.DashboardViewModel
import shared.presentation.viewmodel.LocationsViewModel

fun appModule() = module {
    single<HttpClient> { createHttpClient() }
    single<AirQualityRemoteDataSource> { OpenMeteoAirQualityRemoteDataSource(get()) }
    single<WeatherRemoteDataSource> { OpenMeteoWeatherRemoteDataSource(get()) }
    single<DeviceLocationRemoteDataSource> { IpWhoIsDeviceLocationRemoteDataSource(get()) }
    single<ReverseGeocodingRemoteDataSource> { NominatimReverseGeocodingRemoteDataSource(get()) }
    single<AirQualityRepository> { AirQualityRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<DeviceLocationRepository> { DeviceLocationRepositoryImpl(get()) }
    single<LocationsStorage> { createLocationsStorage() }
    single<LocationsProvider> { InMemoryLocationsProvider(get()) }
    single<ReverseGeocodingRepository> { ReverseGeocodingRepositoryImpl(get()) }
    single<GetDeviceLocationUseCase> { GetDeviceLocationUseCaseImpl(get()) }
    single<GetDashboardDataUseCase> { GetDashboardDataUseCaseImpl(get(), get()) }
    single<GetReverseGeocodingUseCase> { GetReverseGeocodingUseCaseImpl(get()) }
    single { AppViewModel(get(), get()) }
    single { DashboardViewModel(get(), get(), get()) }
    single { LocationsViewModel(get(), get()) }
}
