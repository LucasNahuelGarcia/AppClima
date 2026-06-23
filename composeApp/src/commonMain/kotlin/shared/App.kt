package shared

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shared.presentation.screen.DashboardScreen
import shared.presentation.viewmodel.DashboardViewModel
import shared.domain.model.GeoCoordinates
import shared.domain.usecase.GetDeviceLocationUseCase
import shared.presentation.state.UiState

@Composable
fun App(
    dashboardViewModel: DashboardViewModel,
    getDeviceLocationUseCase: GetDeviceLocationUseCase,
    modifier: Modifier = Modifier
) {
    var refreshKey by remember { mutableIntStateOf(0) }
    val locationState by produceState<UiState<GeoCoordinates>>(initialValue = UiState.Loading, refreshKey) {
        value = getDeviceLocationUseCase()
            .fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error(it.message ?: "No se pudo obtener la ubicacion del dispositivo") }
            )
    }

    when (val currentLocationState = locationState) {
        UiState.Loading -> LocationLoadingView(modifier = modifier)
        is UiState.Error -> LocationErrorView(
            message = currentLocationState.message,
            modifier = modifier,
            onRetry = { refreshKey += 1 }
        )
        is UiState.Success -> DashboardScreen(
            viewModel = dashboardViewModel,
            coordinates = currentLocationState.data,
            refreshKey = refreshKey,
            onRefresh = { refreshKey += 1 },
            modifier = modifier
        )
    }
}

@Composable
private fun LocationLoadingView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Text(
            text = "Obteniendo ubicacion actual...",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Composable
private fun LocationErrorView(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Reintentar")
        }
    }
}
