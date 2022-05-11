plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.plusmobileapps.sample.androiddecompose"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Deps.Android.Compose.VERSION
    }
    packagingOptions {
        resources {
            exclude("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    // compose
    implementation(Deps.Android.Compose.ui)
    implementation(Deps.Android.Compose.foundation)
    implementation(Deps.Android.Compose.material3Design)
    implementation(Deps.Android.Compose.toolingPreview)
    implementation(Deps.Android.Compose.activity)

    // ktx
    implementation(Deps.Android.Ktx.core)
    implementation(Deps.Android.Ktx.lifecycle)

    // architecture
    implementation(Deps.ArkIvanov.MVIKotlin.mvikotlin)
    implementation(Deps.ArkIvanov.MVIKotlin.mvikotlinMain)
    implementation(Deps.ArkIvanov.MVIKotlin.rx)
    implementation(Deps.ArkIvanov.MVIKotlin.mvikotlinLogging)
    implementation(Deps.ArkIvanov.MVIKotlin.mvikotlinExtensionsReaktive)
    implementation(Deps.ArkIvanov.Decompose.decompose)
    implementation(Deps.ArkIvanov.Decompose.extensionsCompose)

    implementation(Deps.Badoo.Reaktive.reaktive)
    implementation(Deps.Badoo.Reaktive.utils)
    implementation(Deps.Badoo.Reaktive.coroutinesInterop)
    testImplementation(Deps.Badoo.Reaktive.reaktiveTesting)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    androidTestImplementation(Deps.Android.Compose.uiTests)
    debugImplementation(Deps.Android.Compose.uiTooling)
    debugImplementation(Deps.Android.Compose.uiTestManifest)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-opt-in=com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi"
    }
}