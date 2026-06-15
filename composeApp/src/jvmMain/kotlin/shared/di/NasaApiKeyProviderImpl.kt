package shared.di

import java.io.File
import java.util.Properties

class NasaApiKeyProviderImpl : NasaApiKeyProvider {
    override fun getKey(): String {
        val envKey = System.getenv("NASA_API_KEY")?.trim().orEmpty()
        if (envKey.isNotEmpty()) {
            return envKey
        }

        val localKey = readKeyFromLocalProperties()
        if (localKey.isNotEmpty()) {
            return localKey
        }

        error(
            "NASA_API_KEY no configurada. Define la variable de entorno NASA_API_KEY " +
                "o agrega NASA_API_KEY=... en local.properties."
        )
    }

    private fun readKeyFromLocalProperties(): String {
        val candidates = listOf(
            File("local.properties"),
            File("composeApp/local.properties"),
            File("../local.properties")
        )

        val file = candidates.firstOrNull { it.exists() } ?: return ""
        val properties = Properties()

        file.inputStream().use { input ->
            properties.load(input)
        }

        return listOf("NASA_API_KEY", "nasa.api.key", "nasaApiKey")
            .firstNotNullOfOrNull { key -> properties.getProperty(key)?.trim()?.takeIf { it.isNotEmpty() } }
            .orEmpty()
    }
}
