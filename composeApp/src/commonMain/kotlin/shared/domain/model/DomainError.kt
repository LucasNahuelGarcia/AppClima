package shared.domain.model

sealed interface DomainError {
    data class DashboardFetchFailed(val cause: Throwable) : Exception(cause), DomainError
}
