package shared.presentation.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun DashboardBackground(
    backgroundResource: DrawableResource,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(backgroundResource),
            contentDescription = "Fondo de clima",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.06f),
                            MaterialTheme.colorScheme.background.copy(alpha = 1f),
                            MaterialTheme.colorScheme.background.copy(alpha = 1f),
                            MaterialTheme.colorScheme.background.copy(alpha = 1f)
                        )
                    )
                )
        )
    }
}