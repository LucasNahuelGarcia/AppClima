package shared.presentation.viewmodel

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import shared.data.repository.DefaultLocationsRepository
import shared.domain.model.GeoCoordinates
import shared.domain.model.LocationData
import shared.fake.FakeResolveLocationUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class LocationsViewModelTest {

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
    fun should_update_add_location_form() = runTest {
        val viewModel = buildViewModel()

        viewModel.startAddingLocation()
        viewModel.onLatitudeChange("-38.7")
        viewModel.onLongitudeChange("-62.2")

        val state = viewModel.uiState.value
        assertTrue(state.isAddingLocation)
        assertEquals("-38.7", state.latitudeText)
        assertEquals("-62.2", state.longitudeText)
        assertEquals(null, state.addLocationError)
    }

    @Test
    fun should_reject_invalid_coordinates() = runTest {
        val viewModel = buildViewModel()

        viewModel.startAddingLocation()
        viewModel.onLatitudeChange("abc")
        viewModel.onLongitudeChange("-62.2")
        viewModel.saveLocation()

        val state = viewModel.uiState.value
        assertTrue(state.isAddingLocation)
        assertEquals("Ingresá latitud y longitud válidas", state.addLocationError)
    }

    @Test
    fun should_save_location_when_reverse_geocoding_succeeds() = runTest {
        val location = buildLocation(GeoCoordinates(-38.7, -62.2))
        val useCase = FakeResolveLocationUseCase(Result.success(location))
        val viewModel = buildViewModel(useCase = useCase)

        viewModel.startAddingLocation()
        viewModel.onLatitudeChange("-38.7")
        viewModel.onLongitudeChange("-62.2")
        viewModel.saveLocation()
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, useCase.calls)
        assertEquals(GeoCoordinates(-38.7, -62.2), useCase.lastCoordinates)
        assertEquals(listOf(location), state.locations)
        assertFalse(state.isAddingLocation)
        assertFalse(state.isSavingLocation)
        assertEquals("", state.latitudeText)
        assertEquals("", state.longitudeText)
        assertEquals(null, state.addLocationError)
    }

    @Test
    fun should_show_error_when_reverse_geocoding_fails() = runTest {
        val error = IllegalStateException("No se pudo resolver")
        val viewModel = buildViewModel(
            useCase = FakeResolveLocationUseCase(Result.failure(error))
        )

        viewModel.startAddingLocation()
        viewModel.onLatitudeChange("-38.7")
        viewModel.onLongitudeChange("-62.2")
        viewModel.saveLocation()
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.isAddingLocation)
        assertFalse(state.isSavingLocation)
        assertEquals("No se pudo resolver", state.addLocationError)
        assertEquals(emptyList(), state.locations)
    }

    @Test
    fun should_remove_location() = runTest {
        val provider = DefaultLocationsRepository()
        val location = buildLocation(GeoCoordinates(-38.7, -62.2))
        provider.saveLocation(location)
        val viewModel = buildViewModel(provider = provider)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.removeLocation(location.coordinates)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(emptyList(), viewModel.uiState.value.locations)
    }

    private fun buildViewModel(
        provider: DefaultLocationsRepository = DefaultLocationsRepository(),
        useCase: FakeResolveLocationUseCase = FakeResolveLocationUseCase(
            Result.success(buildLocation(GeoCoordinates(-38.7, -62.2)))
        )
    ): LocationsViewModel {
        return LocationsViewModel(
            locationsRepository = provider,
            resolveLocationUseCase = useCase
        )
    }

    private fun buildLocation(coordinates: GeoCoordinates): LocationData {
        return LocationData(
            coordinates = coordinates,
            displayName = "Bahía Blanca, Buenos Aires, Argentina",
            locality = "Bahía Blanca",
            region = "Buenos Aires",
            country = "Argentina",
            countryCode = "AR"
        )
    }
}
