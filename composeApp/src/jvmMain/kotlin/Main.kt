import androidx.compose.ui.window.singleWindowApplication
import org.koin.core.context.startKoin
import shared.App
import shared.di.NasaApiKeyProviderImpl
import shared.di.appModule

fun main() {
    val koin = startKoin {
        modules(appModule(NasaApiKeyProviderImpl()))
    }.koin

    singleWindowApplication(title = "Dashboard Clima y Astronomía") {
        App(
            dashboardViewModel = koin.get(),
            getDeviceLocationUseCase = koin.get()
        )
    }
}
