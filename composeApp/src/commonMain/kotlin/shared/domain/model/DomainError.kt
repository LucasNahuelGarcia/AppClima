package shared.domain.model

sealed interface DomainError {
    data class DashboardFetchFailed(override val cause: Throwable?) : Exception(cause), DomainError
    data class ReverseGeocodingFailed(override val cause: Throwable?) : Exception(cause), DomainError
    data class LocationFetchFailed(override val cause: Throwable?) : Exception(cause), DomainError
}
