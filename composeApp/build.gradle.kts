plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm()

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtime.compose)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.contentnegotiation)
                implementation(libs.ktor.serialization.json)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.koin.core)
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.ktor.client.mock)
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.ktor.client.cio)
                implementation(libs.koin.core)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
