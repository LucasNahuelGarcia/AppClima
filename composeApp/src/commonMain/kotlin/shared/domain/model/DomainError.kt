package shared.domain.model

sealed interface DomainError {
    val message: String

    data class DashboardFetchFailed(val cause: Throwable?) : DomainError {
        override val message: String = "Failed to fetch dashboard data."
    }
    data class ReverseGeocodingFailed(val cause: Throwable?) : DomainError {
        override val message: String = "Failed to reverse geocode location."
    }
    data object UnableToGeocode : DomainError {
        override val message: String = "Unable to geocode the provided coordinates."
    }
    data class LocationFetchFailed(val cause: Throwable?) : DomainError {
        override val message: String = "Failed to fetch device location."
    }
    data class AirQualityFetchFailed(val cause: Throwable?) : DomainError {
        override val message: String = "Failed to fetch air quality data."
    }
}