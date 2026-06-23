package shared.domain.model

sealed interface DomainError {
    data class DashboardFetchFailed(override val cause: Throwable?) : Exception(cause), DomainError
    data class ReverseGeocodingFailed(override val cause: Throwable?) : Exception(cause), DomainError
    data object UnableToGeocode : Exception("Unable to geocode"), DomainError
    data class LocationFetchFailed(override val cause: Throwable?) : Exception(cause), DomainError
    data class AirQualityFetchFailed(override val cause: Throwable?) : Exception(cause), DomainError
}
