import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if(localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.wcsm.wcsmfinanceiro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wcsm.wcsmfinanceiro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val exchangeRateApiKey = localProperties["EXCHANGE_RATE_API_KEY"] as? String ?: ""
        buildConfigField("String", "EXCHANGE_RATE_API_KEY", "\"$exchangeRateApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Splashscreen
    implementation(libs.androidx.core.splashscreen)

    // System UI Controller
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")

    // Navigation Component
    implementation(libs.androidx.navigation.compose)

    // JSON Serialization Library
    implementation(libs.kotlinx.serialization.json)

    // Extended Icons
    implementation(libs.androidx.material.icons.extended)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Room
    implementation(libs.androidx.room.runtime)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    kapt(libs.hilt.android.compiler)

    // Gson
    implementation(libs.gson)

    testImplementation(libs.junit)

    // Truth (Unit Tests)
    testImplementation(libs.truth)

    // Turbine for testing Flows (Unit Tests)
    testImplementation(libs.turbine)

    // Mockito
    testImplementation("org.mockito:mockito-core:5.5.0")

    // Coroutine Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Room Test Helper
    androidTestImplementation(libs.androidx.room.testing)

    // Truth (InstrumentedTests)
    androidTestImplementation(libs.truth)

    // Turbine for testing Flows (Instrumented Tests)
    androidTestImplementation(libs.turbine)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}