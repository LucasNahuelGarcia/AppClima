package shared.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource

@Composable
internal fun DashboardHeroSection(
    backgroundResource: DrawableResource,
    viewportHeight: Dp,
    uiState: DashboardUiState,
    presentation: DashboardPresentation?,
    onRefresh: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(viewportHeight)
    ) {
        DashboardBackground(backgroundResource)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.28f))
        )

        Crossfade(
            targetState = uiState,
            label = "DashboardHeroContent"
        ) { state ->
            when (state) {
                DashboardUiState.Loading -> {
                    DashboardLoadingView()
                }
                is DashboardUiState.Error -> {
                    DashboardHeroError(
                        message = state.message,
                        onRetry = onRefresh
                    )
                }
                is DashboardUiState.Content -> {
                    if (presentation != null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp, top = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(onClick = onRefresh) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = null
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            DashboardHeroContent(
                                weather = presentation.weather,
                                locationName = presentation.locationName
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardHeroError(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onRetry) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        DashboardErrorView(
            message = message,
            onRetry = onRetry
        )
    }
}
