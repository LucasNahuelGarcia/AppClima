
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.startKoin
import shared.di.NasaApiKeyProviderImpl
import shared.di.appModule
import shared.presentation.App
import kotlin.system.exitProcess

fun main() = application {
    startKoin {
        modules(appModule(NasaApiKeyProviderImpl()))
    }
    Window(
        onCloseRequest = { exitProcess(0) },
        title = "Dashboard Clima y Astronomía"
    ) {
        App()
    }
}



