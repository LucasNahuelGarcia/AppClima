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
            when (val uiState = state) {
                UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(uiState.message) { viewModel.retry() }
                is UiState.Success -> DashboardContent(uiState.data)
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Composable
private fun DashboardContent(data: DashboardData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        WeatherSection(data.weather)
        AstronomySection(data.astronomy)
    }
}

@Composable
private fun WeatherSection(weather: WeatherData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Weather", style = MaterialTheme.typography.titleMedium)
            Text(text = "Temp (C): ${weather.temperatureCelsius}")
            Text(text = "Wind (km/h): ${weather.windSpeedKmh}")
            Text(text = "Code: ${weather.weatherCode}")
            Text(text = "Time: ${weather.timeIso}")
            Text(text = "Lat: ${weather.coordinates.latitude}, Lon: ${weather.coordinates.longitude}")
        }
    }
}

@Composable
private fun AstronomySection(astronomy: AstronomyData) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Astronomy", style = MaterialTheme.typography.titleMedium)
            Text(text = "Title: ${astronomy.title}")
            Text(text = "Date: ${astronomy.date}")
            Text(text = "Media: ${astronomy.mediaType}")
            Text(text = "Url: ${astronomy.url}")
            Text(text = astronomy.explanation)
        }
    }
}
