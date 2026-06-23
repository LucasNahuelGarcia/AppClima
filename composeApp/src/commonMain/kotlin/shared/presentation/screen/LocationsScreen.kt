package shared.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import shared.domain.model.GeoCoordinates
import shared.domain.repository.LocationsProvider
import shared.domain.usecase.GetReverseGeocodingUseCase
import shared.presentation.locations.LocationsRoute

@Composable
fun LocationsScreen(
    locationsProvider: LocationsProvider,
    getReverseGeocodingUseCase: GetReverseGeocodingUseCase,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locations = locationsProvider.locationsState.collectAsState().value
    val currentLocationCoordinates = locationsProvider.getCurrentLocation()?.coordinates
    val scope = rememberCoroutineScope()
    var isAddingLocation by remember { mutableStateOf(false) }
    var latitudeText by remember { mutableStateOf("") }
    var longitudeText by remember { mutableStateOf("") }
    var addLocationError by remember { mutableStateOf<String?>(null) }
    var isSavingLocation by remember { mutableStateOf(false) }

    fun clearAddLocationForm() {
        latitudeText = ""
        longitudeText = ""
        addLocationError = null
        isAddingLocation = false
    }

    LocationsRoute(
        locations = locations,
        currentLocationCoordinates = currentLocationCoordinates,
        isAddingLocation = isAddingLocation,
        latitudeText = latitudeText,
        longitudeText = longitudeText,
        addLocationError = addLocationError,
        isSavingLocation = isSavingLocation,
        onAddLocationClick = {
            addLocationError = null
            isAddingLocation = true
        },
        onLatitudeChange = {
            latitudeText = it
            addLocationError = null
        },
        onLongitudeChange = {
            longitudeText = it
            addLocationError = null
        },
        onCancelAddLocation = ::clearAddLocationForm,
        onRemoveLocation = { locationsProvider.removeLocation(it) },
        onSaveLocation = {
            val latitude = latitudeText.toDoubleOrNull()
            val longitude = longitudeText.toDoubleOrNull()

            if (latitude == null || longitude == null) {
                addLocationError = "Ingresá latitud y longitud válidas"
                return@LocationsRoute
            }

            scope.launch {
                isSavingLocation = true
                addLocationError = null
                getReverseGeocodingUseCase(
                    GeoCoordinates(
                        latitude = latitude,
                        longitude = longitude
                    )
                ).fold(
                    onSuccess = {
                        locationsProvider.saveLocation(it)
                        clearAddLocationForm()
                    },
                    onFailure = {
                        addLocationError = it.message ?: "No se pudo guardar la ubicación"
                    }
                )
                isSavingLocation = false
            }
        },
        onBack = onBack,
        modifier = modifier
    )
}
