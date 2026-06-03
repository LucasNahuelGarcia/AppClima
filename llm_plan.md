# Plan MVP — Dashboard Clima y Astronomía

## Estado actual
Domain, data y presentation implementados y testeados.
Pendiente: wiring Gradle, DI, entry point, validación end-to-end.

---

## Tareas

### BLOQUE A — Gradle (bloqueante para todo)

#### A1 · `libs.versions.toml`
Agregar versiones y aliases:
- `koin-core`, `koin-compose`
- `ktor-client-core`, `ktor-client-cio`, `ktor-client-content-negotiation`, `ktor-serialization-kotlinx-json`, `ktor-client-logging`
- `ktor-client-mock` (test)
- `kotlinx-serialization-json`
- `kotlinx-coroutines-test` (test)

#### A2 · `build.gradle.kts`
- Plugin `kotlinx-serialization`
- `commonMain`: koin-core, ktor-client-core, ktor-client-content-negotiation, ktor-serialization-kotlinx-json, kotlinx-serialization-json
- `desktopMain`: ktor-client-cio, koin-compose
- `commonTest`: kotlinx-coroutines-test, ktor-client-mock
- `desktopTest`: kotlinx-coroutines-test

---

### BLOQUE B — DI y entry point (requiere A)

#### B1 · `AppModule.kt`
`composeApp/src/commonMain/kotlin/shared/di/AppModule.kt`

Wiring Koin:
```
HttpClient
NasaApiKeyProvider
WeatherRemoteDataSource → OpenMeteoWeatherRemoteDataSource
AstronomyRemoteDataSource → NasaApodRemoteDataSource
WeatherRepository → WeatherRepositoryImpl
AstronomyRepository → AstronomyRepositoryImpl
GetDashboardDataUseCase → GetDashboardDataUseCaseImpl
DashboardViewModel
```

#### B2 · `HttpClient` factory
`composeApp/src/commonMain/kotlin/shared/di/HttpClientFactory.kt`

Configurar:
- `ContentNegotiation` con `kotlinx.serialization`
- `Logging` (level DEBUG en dev)
- Timeouts razonables

#### B3 · `NasaApiKeyProvider`
Interface en `commonMain`, impl en `desktopMain`.

`composeApp/src/commonMain/kotlin/shared/di/NasaApiKeyProvider.kt`
- Interface con `fun getKey(): String`

`composeApp/src/desktopMain/kotlin/shared/di/NasaApiKeyProviderImpl.kt`
- Lee `System.getenv("NASA_API_KEY")` o `local.properties`
- Lanza excepción clara si key ausente

`main.kt` instancia `NasaApiKeyProviderImpl` y lo pasa a Koin:
```kotlin
startKoin {
    modules(appModule(NasaApiKeyProviderImpl()))
}
```

Documentar en README cómo setear la key antes de correr.

> Alternativa: plugin `BuildKonfig` — lee `local.properties` en compile-time y genera objeto Kotlin en `commonMain`. Más seguro si el proyecto crece a más plataformas.

#### B4 · `main.kt` + `App.kt`
`composeApp/src/desktopMain/kotlin/main.kt`
`composeApp/src/desktopMain/kotlin/App.kt`

- `startKoin { modules(appModule) }`
- `singleWindowApplication` con `DashboardScreen`
- Coordenadas default: Bahía Blanca (-38.7183, -62.2663)

---

### BLOQUE C — UI mínima (paralelo con B)

#### C1 · Coordenadas en `DashboardScreen`
- Mostrar lat/lon actuales en pantalla
- Botón o campo para cambiar coordenadas (opcional MVP)
- Default hardcodeado a Bahía Blanca si no hay input

#### C2 · Verificar los 3 estados visuales
- `Loading` → indicador visible
- `Success` → datos de clima + astronomía renderizados
- `Error` → mensaje + botón retry funcional

#### C3 · Composables básicos de datos
En `DashboardScreen` o componentes separados en `presentation/component/`:
- `WeatherCard` — temperatura, condición, humedad
- `AstronomyCard` — título APOD, imagen, descripción

---

### BLOQUE D — Validación end-to-end (requiere A + B + C)

#### D1 · Smoke test Open-Meteo
- Correr app con coordenadas reales
- Verificar respuesta HTTP 200
- Verificar mapeo correcto a `WeatherData`

#### D2 · Smoke test NASA APOD
- Verificar API key cargada correctamente
- Verificar respuesta HTTP 200
- Verificar mapeo correcto a `AstronomyData`

#### D3 · Fix de integración
- Ajustar endpoints si difieren del contrato asumido en DTOs
- Ajustar mappers si campos son opcionales o tienen nombres distintos
- Manejar caso APOD de tipo `video` (no tiene `url` de imagen)

---

## Orden de ejecución recomendado

```
A1 → A2 → B1 → B2 → B3 → B4
                          ↓
                     C1 + C2 + C3 (paralelo)
                          ↓
                     D1 → D2 → D3
```

A1 y A2 los hace una sola persona. B y C pueden dividirse en paralelo una vez desbloqueado A.

---

## División sugerida entre personas

| Persona | Tareas |
|---|---|
| 1 | A1, A2 (bloqueante — hacer primero) |
| 2 | B1, B2, B3, B4 |
| 3 | C1, C2, C3 |
| 1 o 2 | D1, D2, D3 (todos juntos al final) |

---

## Notas
- NASA API key nunca va a `git commit`. Agregar a `.gitignore`: `.env`, `local.properties`
- Documentar en `README.md` cómo configurar la key antes de correr
- `ktor-client-mock` ya usado en tests de data layer — verificar que la versión en `libs.versions.toml` matchee