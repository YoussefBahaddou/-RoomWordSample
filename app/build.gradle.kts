plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.emsi.roomwordsample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.emsi.roomwordsample"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Dépendances pour travailler avec les composants d'architecture
    // Vous devrez probablement mettre à jour les numéros de version dans libs.versions.toml
    // Composant de base de données Room
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)
    testImplementation(libs.room.testing)

    // Composants de cycle de vie
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.common.java8)
}
