package shared.presentation.dashboard

import androidx.compose.runtime.Composable

@Composable
internal fun DashboardHeroContent(
    uiState: DashboardUiState,
    presentation: DashboardPresentation?,
    onRetry: () -> Unit
) {
    when (uiState) {
        DashboardUiState.Loading -> DashboardLoadingView()
        is DashboardUiState.Error -> DashboardErrorView(
            message = uiState.message,
            onRetry = onRetry
        )
        is DashboardUiState.Content -> {
            val content = presentation ?: return
            DashboardHeroWeatherCard(
                weather = content.weather,
                locationName = content.locationName
            )
        }
    }
}
