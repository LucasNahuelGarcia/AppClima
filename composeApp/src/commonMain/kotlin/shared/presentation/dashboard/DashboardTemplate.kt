package shared.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun DashboardTemplate(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    val presentation = (uiState as? DashboardUiState.Content)?.presentation
    val backgroundResource = dashboardBackgroundResource(presentation)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val viewportHeight = maxHeight

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DashboardHeroSection(
                    backgroundResource = backgroundResource,
                    viewportHeight = viewportHeight,
                    uiState = uiState,
                    presentation = presentation,
                    onRefresh = onRefresh
                )
            }

            item {
                if (presentation != null) {
                    DashboardDetailsSection(presentation)
                } else {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(24.dp)
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
            }
        }
    }
}
