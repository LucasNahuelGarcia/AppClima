package shared.fake

import shared.domain.model.GeoCoordinates
import shared.domain.usecase.GetApproximateDeviceLocationUseCase

class FakeGetApproximateDeviceLocationUseCase(
    private val results: ArrayDeque<Result<GeoCoordinates>>
) : GetApproximateDeviceLocationUseCase {
    var calls = 0

    constructor(result: Result<GeoCoordinates>) : this(ArrayDeque(listOf(result)))

    override suspend fun invoke(): Result<GeoCoordinates> {
        calls++
        return if (results.isEmpty()) {
            error("Missing GeoCoordinates result")
        } else {
            results.removeFirst()
        }
    }
}
