package shared.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun DashboardLoadingView() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background) 
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onSurface)
            Text(
                text = "Consultando",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Black
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
internal fun DashboardErrorView(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.background) 
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(onClick = onRetry) {
                Text(text = "Actualizar datos")
            }
        }
    }
}