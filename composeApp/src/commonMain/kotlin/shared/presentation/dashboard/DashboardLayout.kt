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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun DashboardLayout(
    uiState: DashboardUiState,
    currentPage: Int,
    pageCount: Int,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit,
    onOpenLocationsWindow: () -> Unit
) {
    val presentation = (uiState as? DashboardUiState.Content)?.presentation
    var backgroundResource by remember { mutableStateOf(dashboardBackgroundResource(presentation)) }

    LaunchedEffect(presentation) {
        if (presentation != null) {
            backgroundResource = dashboardBackgroundResource(presentation)
        }
    }

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
                    currentPage = currentPage,
                    pageCount = pageCount,
                    onRefresh = onRefresh,
                    onOpenLocationsWindow = onOpenLocationsWindow
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
