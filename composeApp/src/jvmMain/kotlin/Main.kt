import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.koin.core.context.startKoin
import shared.App
import shared.di.appModule

fun main() {
    val koin = startKoin {
        modules(appModule())
    }.koin

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Dashboard Clima y Astronomía",
            state = rememberWindowState(size = DpSize(450.dp, 844.dp))
        ) {
            App(
                dashboardViewModel = koin.get(),
                getDeviceLocationUseCase = koin.get(),
                getReverseGeocodingUseCase = koin.get(),
                locationsProvider = koin.get()
            )
        }
    }
}
