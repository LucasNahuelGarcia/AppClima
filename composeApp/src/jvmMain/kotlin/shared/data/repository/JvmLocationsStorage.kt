package shared.data.repository

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import shared.domain.model.LocationData

private const val APP_STORAGE_DIRECTORY = ".clima-astronomia"
private const val LOCATIONS_FILE_NAME = "locations.json"

actual fun createLocationsStorage(): LocationsStorage {
    val userHome = System.getProperty("user.home")
    val storageFile = Path.of(userHome, APP_STORAGE_DIRECTORY, LOCATIONS_FILE_NAME)
    return JvmLocationsStorage(storageFile)
}

class JvmLocationsStorage(
    private val storageFile: Path
) : LocationsStorage {

    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true
    }

    override fun readSavedLocations(): List<LocationData> {
        return runCatching {
            if (!storageFile.exists()) {
                return emptyList()
            }

            json.decodeFromString<SavedLocationsDto>(storageFile.readText())
                .locations
                .map { it.toDomainModel() }
        }.getOrDefault(emptyList())
    }

    override fun writeSavedLocations(locations: List<LocationData>) {
        runCatching {
            storageFile.parent?.createDirectories()
            val dto = SavedLocationsDto(
                locations = locations.map { it.toSavedLocationDto() }
            )
            storageFile.writeText(json.encodeToString(dto))
        }
    }
}
