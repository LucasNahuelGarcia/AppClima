import androidx.compose.runtime.Composable
import androidx.compose.ui.window.singleWindowApplication
import org.koin.core.context.startKoin
import shared.di.NasaApiKeyProviderImpl
import shared.di.appModule
import shared.domain.model.GeoCoordinates
import shared.presentation.screen.DashboardScreen
import shared.presentation.viewmodel.DashboardViewModel

private val defaultCoordinates = GeoCoordinates(
    latitude = -38.7183,
    longitude = -62.2663
)

@Composable
fun App(dashboardViewModel: DashboardViewModel) {
    DashboardScreen(
        viewModel = dashboardViewModel,
        coordinates = defaultCoordinates
    )
}

fun main() {
    val koin = startKoin {
        modules(appModule(NasaApiKeyProviderImpl()))
    }.koin

    singleWindowApplication(title = "Dashboard Clima y Astronomía") {
        App(dashboardViewModel = koin.get())
    }
}