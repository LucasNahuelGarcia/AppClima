package shared.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import shared.data.repository.InMemoryLocationsProvider
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.fake.FakeGetDeviceLocationUseCase
import shared.presentation.state.UiState

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun should_load_current_location_on_init() = runTest {
        val coordinates = GeoCoordinates(-38.7, -62.2)
        val useCase = FakeGetDeviceLocationUseCase(Result.success(coordinates))
        val viewModel = AppViewModel(useCase, InMemoryLocationsProvider())

        dispatcher.scheduler.advanceUntilIdle()

        val state = assertIs<UiState.Success<GeoCoordinates>>(viewModel.uiState.value.currentLocationState)
        assertEquals(coordinates, state.data)
        assertEquals(1, useCase.calls)
    }

    @Test
    fun should_show_error_when_current_location_fails() = runTest {
        val viewModel = AppViewModel(
            getDeviceLocationUseCase = FakeGetDeviceLocationUseCase(
                Result.failure(IllegalStateException("location failed"))
            ),
            locationsProvider = InMemoryLocationsProvider()
        )

        dispatcher.scheduler.advanceUntilIdle()

        val state = assertIs<UiState.Error>(viewModel.uiState.value.currentLocationState)
        assertEquals("location failed", state.message)
    }

    @Test
    fun should_refresh_current_location_and_increment_refresh_key() = runTest {
        val firstCoordinates = GeoCoordinates(-38.7, -62.2)
        val secondCoordinates = GeoCoordinates(-34.6, -58.4)
        val useCase = FakeGetDeviceLocationUseCase(
            ArrayDeque(
                listOf(
                    Result.success(firstCoordinates),
                    Result.success(secondCoordinates)
                )
            )
        )
        val viewModel = AppViewModel(useCase, InMemoryLocationsProvider())
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.refresh()
        dispatcher.scheduler.advanceUntilIdle()

        val state = assertIs<UiState.Success<GeoCoordinates>>(viewModel.uiState.value.currentLocationState)
        assertEquals(secondCoordinates, state.data)
        assertEquals(1, viewModel.uiState.value.refreshKey)
        assertEquals(2, useCase.calls)
    }

    @Test
    fun should_toggle_locations_screen_and_track_dashboard_page() = runTest {
        val viewModel = AppViewModel(
            FakeGetDeviceLocationUseCase(Result.success(GeoCoordinates(-38.7, -62.2))),
            InMemoryLocationsProvider()
        )

        viewModel.openLocationsScreen()
        viewModel.onDashboardPageChanged(2)
        assertTrue(viewModel.uiState.value.showLocationsScreen)
        assertEquals(2, viewModel.uiState.value.currentDashboardPage)

        viewModel.closeLocationsScreen()
        assertFalse(viewModel.uiState.value.showLocationsScreen)
    }

    @Test
    fun should_publish_locations_from_provider() = runTest {
        val provider = InMemoryLocationsProvider()
        val viewModel = AppViewModel(
            FakeGetDeviceLocationUseCase(Result.success(GeoCoordinates(-38.7, -62.2))),
            provider
        )
        val location = buildLocation(GeoCoordinates(-34.6, -58.4))

        provider.saveLocation(location)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(listOf(location), viewModel.uiState.value.locations)
    }

    private fun buildLocation(coordinates: GeoCoordinates): LocationData {
        return LocationData(
            coordinates = coordinates,
            displayName = "Buenos Aires, Argentina",
            locality = "Buenos Aires",
            region = "Ciudad Autónoma de Buenos Aires",
            country = "Argentina",
            countryCode = "AR"
        )
    }
}
