package shared.presentation

import androidx.compose.runtime.Composable
import org.koin.core.context.GlobalContext
import shared.domain.model.GeoCoordinates
import shared.presentation.screen.DashboardScreen
import shared.presentation.viewmodel.DashboardViewModel

@Composable
fun App() {
    val viewModel: DashboardViewModel = GlobalContext.get().get()
    val bahiaBlancaCoordinates = GeoCoordinates(
        latitude = -38.7183,
        longitude = -62.2663
    )
    DashboardScreen(
        viewModel = viewModel,
        coordinates = bahiaBlancaCoordinates
    )
}

