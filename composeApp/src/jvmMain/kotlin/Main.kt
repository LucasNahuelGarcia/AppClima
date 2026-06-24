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
            resizable = false,
            state = rememberWindowState(size = DpSize(450.dp, 844.dp))
        ) {
            App(
                mainViewModel = koin.get(),
                dashboardViewModel = koin.get(),
                locationsViewModel = koin.get()
            )
        }
    }
}
