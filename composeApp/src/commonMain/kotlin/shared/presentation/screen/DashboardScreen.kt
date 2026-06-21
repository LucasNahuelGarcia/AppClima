package shared.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import shared.domain.model.*
import shared.presentation.state.UiState
import shared.presentation.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    coordinates: GeoCoordinates,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    // TODO: SERVICIO INVENTADO. Conectar a un LocalTime.now() real del sistema o al backend.
    val isNightTime = remember { true }

    LaunchedEffect(coordinates) {
        viewModel.loadDashboard(coordinates)
    }

    MaterialTheme(
        colorScheme = if (isNightTime) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                when (val uiState = state) {
                    UiState.Loading -> LoadingView()
                    is UiState.Error -> ErrorView(uiState.message) { viewModel.retry() }
                    is UiState.Success<*> -> DashboardContent(
                        data = uiState.data as DashboardData,
                        locationState = locationState
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DashboardContent(
    data: DashboardData,
    locationState: UiState<LocationData>
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Bloque superior: Hero clima animado.
        HeroWeatherCard(
            weather = data.weather,
            locationName = locationState.locationNameOrFallback(data.weather.locationName)
        )

        // Bloque intermedio: Pronóstico horizontal.
        TodayForecastSection(data.weather.hourlyForecast)

        // Bloque inferior: Tarjeta lunar.
        MoonPhaseCard(data.astronomy.moonPhase)
    }
}

@Composable
private fun LoadingView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CircularProgressIndicator()
        Text(
            text = "Consultando satélites...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
            Button(onClick = onRetry) {
                Text(text = "Reintentar")
            }
        }
    }
}

@Composable
private fun HeroWeatherCard(
    weather: WeatherData,
    locationName: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Columna Izquierda: Textos.
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // <Ubicación actual> - <fecha/hora>
                Text(
                    text = "$locationName  •  ${weather.formattedDate}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // <(Grande) Temperatura>
                Text(
                    text = "${weather.temperatureCelsius}°",
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 68.sp,
                        fontWeight = FontWeight.Black
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // <(pequeño con icono) Viento>
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Air,
                        contentDescription = "Velocidad del viento",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "${weather.windSpeedKmh} km/h",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Columna Derecha: Espacio reservado para GIF / Secuencia.
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                KamelImage(
                    resource = asyncPainterResource(data = weather.weatherAnimUrl),
                    contentDescription = "Animación del clima en tiempo real",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onFailure = {
                        // Fallback visual si el backend aún no manda el GIF.
                        Text("GIF Clima", style = MaterialTheme.typography.labelSmall)
                    }
                )
            }
        }
    }
}

private fun UiState<LocationData>.locationNameOrFallback(fallback: String): String {
    return when (this) {
        UiState.Loading -> "Resolviendo ubicación"
        is UiState.Error -> fallback
        is UiState.Success -> data.displayName.ifBlank { fallback }
    }
}

@Composable
private fun TodayForecastSection(forecasts: List<HourlyForecast>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Pronóstico hoy:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(forecasts) { item ->
                HourlyCardItem(item)
            }
        }
    }
}

@Composable
private fun HourlyCardItem(item: HourlyForecast) {
    Card(
        modifier = Modifier.width(72.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = item.time, style = MaterialTheme.typography.labelSmall)

            KamelImage(
                resource = asyncPainterResource(data = item.iconUrl),
                contentDescription = "Clima a las ${item.time}",
                modifier = Modifier.size(32.dp)
            )

            Text(
                text = "${item.temperatureCelsius}°",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MoonPhaseCard(moonPhase: MoonPhaseData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KamelImage(
                resource = asyncPainterResource(data = moonPhase.iconUrl),
                contentDescription = "Fase de la luna: ${moonPhase.phaseName}",
                modifier = Modifier.size(54.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Fase lunar actual",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
                Text(
                    text = moonPhase.phaseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "Iluminación visible: ${moonPhase.illuminationPercent}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
