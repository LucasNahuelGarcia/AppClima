package shared.presentation.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import shared.domain.model.AirQualityData
import shared.domain.model.AirQualityLevel

@Composable
internal fun DashboardAirQualityCard(airQuality: AirQualityData?) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.secondaryContainer
		)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(16.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(16.dp)
		) {
			Icon(
				imageVector = Icons.Default.Air,
				contentDescription = "Calidad del aire",
				modifier = Modifier.size(40.dp),
				tint = MaterialTheme.colorScheme.onSecondaryContainer
			)

			Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
				Text(
					text = "Calidad del aire",
					style = MaterialTheme.typography.labelMedium,
					color = MaterialTheme.colorScheme.onSecondaryContainer
				)

				if (airQuality == null || airQuality.level == AirQualityLevel.Unknown) {
					Text(
						text = "Datos no disponibles",
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.Bold,
						color = MaterialTheme.colorScheme.onSecondaryContainer
					)
					Text(
						text = "Mostraremos el índice cuando esté disponible.",
						style = MaterialTheme.typography.bodySmall,
						color = MaterialTheme.colorScheme.onSecondaryContainer
					)
				} else {
					Text(
						text = "AQI ${airQuality.europeanAqi ?: "—"} · ${airQuality.level.displayName}",
						style = MaterialTheme.typography.titleMedium,
						fontWeight = FontWeight.Bold,
						color = MaterialTheme.colorScheme.onSecondaryContainer
					)
					Text(
						text = airQuality.level.description,
						style = MaterialTheme.typography.bodySmall,
						color = MaterialTheme.colorScheme.onSecondaryContainer
					)
					Text(
						text = "PM2.5 ${airQuality.pm2_5?.roundToInt() ?: "—"} µg/m³ · PM10 ${airQuality.pm10?.roundToInt() ?: "—"} µg/m³",
						style = MaterialTheme.typography.bodySmall,
						color = MaterialTheme.colorScheme.onSecondaryContainer
					)
				}
			}
		}
	}
}


