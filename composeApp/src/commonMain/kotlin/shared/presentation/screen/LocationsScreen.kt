package shared.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import shared.presentation.locations.LocationsRoute
import shared.presentation.viewmodel.LocationsViewModel

@Composable
fun LocationsScreen(
    viewModel: LocationsViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState = viewModel.uiState.collectAsState().value

    LocationsRoute(
        locations = uiState.locations,
        currentLocationCoordinates = uiState.currentLocationCoordinates,
        isAddingLocation = uiState.isAddingLocation,
        latitudeText = uiState.latitudeText,
        longitudeText = uiState.longitudeText,
        addLocationError = uiState.addLocationError,
        isSavingLocation = uiState.isSavingLocation,
        onAddLocationClick = viewModel::startAddingLocation,
        onLatitudeChange = viewModel::onLatitudeChange,
        onLongitudeChange = viewModel::onLongitudeChange,
        onCancelAddLocation = viewModel::cancelAddLocation,
        onRemoveLocation = viewModel::removeLocation,
        onSaveLocation = viewModel::saveLocation,
        onBack = onBack,
        modifier = modifier
    )
}
