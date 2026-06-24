package shared.domain.model

class DomainException(val domainError: DomainError, cause: Throwable? = null) : Exception(cause)
