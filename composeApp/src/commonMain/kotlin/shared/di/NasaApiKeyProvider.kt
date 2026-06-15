package shared.di

fun interface NasaApiKeyProvider {
    fun getKey(): String
}