package shared.di

import java.io.File

class NasaApiKeyProviderImpl : NasaApiKeyProvider {
    override fun getKey(): String {
        return System.getenv("NASA_API_KEY")
            ?: loadFromLocalProperties()
            ?: throw IllegalStateException(
                "NASA_API_KEY no encontrada. " +
                        "Configura la variable de entorno NASA_API_KEY o agrega la clave en local.properties"
            )
    }

    private fun loadFromLocalProperties(): String? {
        // Búsqueda en múltiples rutas posibles
        val possiblePaths = listOf(
            File("local.properties"),
            File(System.getProperty("user.dir"), "local.properties"),
            File(System.getProperty("user.home"), "Dev/local.properties"),
            File("..", "local.properties").canonicalFile
        )

        for (file in possiblePaths) {
            if (file.exists()) {
                val key = file.useLines { lines ->
                    lines
                        .filter { it.startsWith("nasa.api.key=") && !it.trim().startsWith("#") }
                        .map { it.removePrefix("nasa.api.key=").trim() }
                        .firstOrNull()
                        ?.takeIf { it.isNotEmpty() }
                }
                if (key != null) return key
            }
        }
        return null
    }
}

