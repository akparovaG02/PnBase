plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.pnbase"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.pnbase"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
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
    buildFeatures {
        compose = true
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
    implementation(libs.firebase.database)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("io.insert-koin:koin-android:3.5.0")

    // Koin для Compose (если используешь Compose)
    implementation ("io.insert-koin:koin-androidx-compose:3.5.0")

    implementation ("io.insert-koin:koin-androidx-navigation:3.5.0")
    testImplementation ("io.insert-koin:koin-test-junit4:3.5.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation ("androidx.work:work-runtime-ktx:2.10.1")
    implementation("androidx.work:work-runtime-ktx:2.10.1")
    implementation ("androidx.navigation:navigation-compose:2.9.0")

    implementation("androidx.compose.runtime:runtime:1.8.2")
    implementation("androidx.compose.runtime:runtime-livedata:1.8.2")
    implementation("androidx.compose.runtime:runtime-rxjava2:1.8.2")

    implementation ("com.google.firebase:firebase-auth-ktx:22.3.1")
    implementation ("com.google.firebase:firebase-database-ktx:20.3.1")
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.android.gms:play-services-gcm:16.0.0")

    implementation("com.google.firebase:firebase-messaging:23.4.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("io.coil-kt:coil-compose:2.4.0")

    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt ("androidx.lifecycle:lifecycle-compiler:2.9.1")

    implementation ("androidx.room:room-runtime:2.7.1")
    kapt ("androidx.room:room-compiler:2.7.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.1")

    implementation ("androidx.paging:paging-runtime:3.3.6")

    implementation ("com.google.accompanist:accompanist-pager:0.34.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.34.0")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")

    implementation ("com.google.android.gms:play-services-location:20.0.0")
}