plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt") // Necesario para Room
    // id("androidx.navigation.safeargs.kotlin") // Lo activaremos más adelante cuando configuremos el classpath
}

android {
    namespace = "com.example.agendadam"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.agendadam"
        minSdk = 24
        targetSdk = 36
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
        viewBinding = true
    }
}

dependencies {
    // --- NAVEGACIÓN (Tema 9) [cite: 2805, 3091] ---
    val nav_version = "2.8.0" // O la versión más reciente compatible
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // --- BASE DE DATOS ROOM (Tema 10) [cite: 1969, 1971, 1980] ---
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version") // Para usar Corrutinas con Room
    kapt("androidx.room:room-compiler:$room_version")

    // --- RETROFIT & GSON (Tema 6) [cite: 411, 412] ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // --- IMÁGENES (Picasso) (Tema 6) [cite: 410] ---
    implementation("com.squareup.picasso:picasso:2.71828")

    // Dependencias base (ya deberían estar)
    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.9.0")
}