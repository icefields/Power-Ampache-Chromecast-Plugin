plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "luci.sixsixsix.powerampache2.chromecastplugin"
    compileSdk = 35

    defaultConfig {
        applicationId = "luci.sixsixsix.powerampache2.chromecastplugin"
        minSdk = 30
        targetSdk = 35
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.lifecycle.viewmodel)
    implementation(libs.androidx.activity.compose) // Required for setContent
    implementation(libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)

    // JSON serialization
    implementation(libs.gson)
    // Google Cast framework
    implementation (libs.play.services.cast.framework)

    // --- Coil, image-loader --- //
    implementation(libs.coil.compose)

    // --- Dagger Hilt --- //
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.common)
}
