import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val apiKey = localProperties.getProperty("LOCATION_API_KEY") ?: ""

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "kr.android.shoppinglistapp_room"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "kr.android.shoppinglistapp_room"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "LOCATION_API_KEY", "\"$apiKey\"")
        manifestPlaceholders["LOCATION_API_KEY"] = apiKey
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.material3)
    val nav_version = "2.7.5"
    val room_version = "2.7.0"

    // Compose BOM (MUST be first)
    implementation(platform(libs.androidx.compose.bom))

    // 🗺️ Google Maps + Location (UI maps + GPS access)
    // Compose integration for Google Maps (Google Maps composable)
    implementation("com.google.maps.android:maps-compose:4.3.0")

    // Core Google Maps SDK (renders map, markers, camera, etc.)
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Provides location services (GPS, fused location provider)
    implementation("com.google.android.gms:play-services-location:21.2.0")

    // 🌐 Networking (API calls + JSON parsing)
    // HTTP client for making REST API calls (GET, POST, etc.)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Converts JSON responses into Kotlin data classes using Gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

    // Icons
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Room (KSP)
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}