plugins {
    id("com.android.application")
    kotlin("android")
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

    implementation(Deps.Android.Compose.ui)
    implementation(Deps.Android.Compose.foundation)
    implementation(Deps.Android.Compose.material3Design)
    implementation(Deps.Android.Compose.toolingPreview)
    implementation(Deps.Android.Compose.activity)

    implementation(Deps.Android.Ktx.core)
    implementation(Deps.Android.Ktx.lifecycle)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    androidTestImplementation(Deps.Android.Compose.uiTests)
    debugImplementation(Deps.Android.Compose.uiTooling)
    debugImplementation(Deps.Android.Compose.uiTestManifest)
}