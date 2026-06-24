package shared.di

import io.ktor.client.HttpClient
import org.koin.dsl.module
import shared.data.datasource.remote.AirQualityRemoteDataSource
import shared.data.datasource.remote.ApproximateDeviceLocationRemoteDataSource
import shared.data.datasource.remote.IpWhoIsApproximateDeviceLocationRemoteDataSource
import shared.data.datasource.remote.NominatimReverseGeocodingRemoteDataSource
import shared.data.datasource.remote.OpenMeteoAirQualityRemoteDataSource
import shared.data.datasource.remote.OpenMeteoWeatherRemoteDataSource
import shared.data.datasource.remote.ReverseGeocodingRemoteDataSource
import shared.data.datasource.remote.WeatherRemoteDataSource
import shared.data.repository.AirQualityRepositoryImpl
import shared.data.repository.DefaultLocationsRepository
import shared.data.repository.ApproximateDeviceLocationRepositoryImpl
import shared.data.repository.LocationsStorage
import shared.data.repository.ReverseGeocodingRepositoryImpl
import shared.data.repository.WeatherRepositoryImpl
import shared.data.repository.createLocationsStorage
import shared.domain.repository.AirQualityRepository
import shared.domain.repository.ApproximateDeviceLocationRepository
import shared.domain.repository.LocationsRepository
import shared.domain.repository.ReverseGeocodingRepository
import shared.domain.repository.WeatherRepository
import shared.domain.usecase.GetApproximateDeviceLocationUseCase
import shared.domain.usecase.GetApproximateDeviceLocationUseCaseImpl
import shared.domain.usecase.GetDashboardDataUseCase
import shared.domain.usecase.GetDashboardDataUseCaseImpl
import shared.domain.usecase.ResolveLocationUseCase
import shared.domain.usecase.ResolveLocationUseCaseImpl
import shared.presentation.viewmodel.MainViewModel
import shared.presentation.viewmodel.DashboardViewModel
import shared.presentation.viewmodel.LocationsViewModel

fun appModule() = module {
    single<HttpClient> { createHttpClient() }
    single<AirQualityRemoteDataSource> { OpenMeteoAirQualityRemoteDataSource(get()) }
    single<WeatherRemoteDataSource> { OpenMeteoWeatherRemoteDataSource(get()) }
    single<ApproximateDeviceLocationRemoteDataSource> { IpWhoIsApproximateDeviceLocationRemoteDataSource(get()) }
    single<ReverseGeocodingRemoteDataSource> { NominatimReverseGeocodingRemoteDataSource(get()) }
    single<AirQualityRepository> { AirQualityRepositoryImpl(get()) }
    single<WeatherRepository> { WeatherRepositoryImpl(get()) }
    single<ApproximateDeviceLocationRepository> { ApproximateDeviceLocationRepositoryImpl(get()) }
    single<LocationsStorage> { createLocationsStorage() }
    single<LocationsRepository> { DefaultLocationsRepository(get()) }
    single<ReverseGeocodingRepository> { ReverseGeocodingRepositoryImpl(get()) }
    single<GetApproximateDeviceLocationUseCase> { GetApproximateDeviceLocationUseCaseImpl(get()) }
    single<GetDashboardDataUseCase> { GetDashboardDataUseCaseImpl(get(), get()) }
    single<ResolveLocationUseCase> { ResolveLocationUseCaseImpl(get()) }
    single { MainViewModel(get(), get()) }
    single { DashboardViewModel(get(), get(), get()) }
    single { LocationsViewModel(get(), get()) }
}
