object Deps {
    object Jetbrains {
        const val KOTLIN_VERSION = "1.6.10"
        const val COROUTINES_VERSION = "1.6.0"

        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$KOTLIN_VERSION"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION"
        const val coroutinesTesting = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$COROUTINES_VERSION"
    }


    object Android {
        const val androidGradlePlugin = "com.android.tools.build:gradle:7.0.2"

        object Ktx {
            const val core = "androidx.core:core-ktx:1.7.0"
            const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.4.1"
            const val activity = "androidx.activity:activity-ktx:1.2.3"
        }

        const val coil = "io.coil-kt:coil-compose:2.0.0"

        object Compose {
            const val VERSION = "1.1.1"
            const val activity = "androidx.activity:activity-compose:1.3.1"
            const val ui = "androidx.compose.ui:ui:$VERSION"
            // Tooling support (Previews, etc.)
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$VERSION"
            // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
            const val foundation = "androidx.compose.foundation:foundation:$VERSION"
            // Material Design
            const val material3Design = "androidx.compose.material3:material3:1.0.0-alpha01"
            const val uiTooling = "androidx.compose.ui:ui-tooling:$VERSION"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$VERSION"
            // UI Tests
            const val uiTests = "androidx.compose.ui:ui-test-junit4:$VERSION"


        }
    }

    object SqlDelight {
        const val VERSION = "1.5.3"
        const val gradlePlugin = "com.squareup.sqldelight:gradle-plugin:$VERSION"
        const val driver =  "com.squareup.sqldelight:android-driver:$VERSION"
        const val coroutines = "com.squareup.sqldelight:coroutines-extensions-jvm:$VERSION"
    }

    object ArkIvanov {
        object MVIKotlin {
            private const val VERSION = "3.0.0-beta02"
            const val rx = "com.arkivanov.mvikotlin:rx:$VERSION"
            const val mvikotlin = "com.arkivanov.mvikotlin:mvikotlin:$VERSION"
            const val mvikotlinMain = "com.arkivanov.mvikotlin:mvikotlin-main:$VERSION"
            const val mvikotlinLogging = "com.arkivanov.mvikotlin:mvikotlin-logging:$VERSION"
            const val mvikotlinExtensionsCoroutines = "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:$VERSION"
        }

        object Decompose {
            private const val VERSION = "0.6.0"
            const val decompose = "com.arkivanov.decompose:decompose:$VERSION"
            const val extensionsCompose = "com.arkivanov.decompose:extensions-compose-jetpack:$VERSION"
        }
    }

    object Square {
        object Retrofit {
            const val VERSION = "2.9.0"
            const val core = "com.squareup.retrofit2:retrofit:$VERSION"
            const val gson = "com.squareup.retrofit2:converter-gson:$VERSION"
        }
    }

    object Mockk {
        const val VERSION = "1.12.4"
        const val core = "io.mockk:mockk:$VERSION"
    }

}