package shared.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shared.domain.model.AstronomyData
import shared.domain.model.DashboardData
import shared.domain.model.GeoCoordinates
import shared.domain.model.WeatherData
import shared.presentation.state.UiState
import shared.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    coordinates: GeoCoordinates,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(coordinates) {
        viewModel.loadDashboard(coordinates)
    }

    MaterialTheme {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CoordinatesBanner(coordinates)
                when (val uiState = state) {
                    UiState.Loading -> LoadingView()
                    is UiState.Error -> ErrorView(uiState.message) { viewModel.retry() }
                    is UiState.Success<*> -> DashboardContent(uiState.data as DashboardData)
                }
            }
        }
    }
}

@Composable
private fun CoordinatesBanner(coordinates: GeoCoordinates) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = "Coordenadas", style = MaterialTheme.typography.titleMedium)
            Text(text = "Latitud: ${coordinates.latitude}")
            Text(text = "Longitud: ${coordinates.longitude}")
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry) {
            Text(text = "Reintentar")
        }
    }
}

@Composable
private fun DashboardContent(data: DashboardData) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherCard(data.weather)
        AstronomyCard(data.astronomy)
    }
}

@Composable
private fun WeatherCard(weather: WeatherData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Clima", style = MaterialTheme.typography.titleMedium)
            Text(text = "Temperatura: ${weather.temperatureCelsius} °C")
            Text(text = "Viento: ${weather.windSpeedKmh} km/h")
            Text(text = "Código meteorológico: ${weather.weatherCode}")
            Text(text = "Hora: ${weather.timeIso}")
        }
    }
}

@Composable
private fun AstronomyCard(astronomy: AstronomyData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Astronomía", style = MaterialTheme.typography.titleMedium)
            Text(text = astronomy.title)
            Text(text = "Tipo de media: ${astronomy.mediaType}")
            Text(text = "Fecha: ${astronomy.date}")
            Text(text = astronomy.explanation)
            Text(text = "URL: ${astronomy.url}")
        }
    }
}
