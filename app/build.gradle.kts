plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

    // GOOGLE SERVICES
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.laposada"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.laposada"
        minSdk = 29
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
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3)
    implementation("com.google.android.material:material:1.x.x")

    // GOOGLE SERVICES FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
    // FIREBASE AUTH
    implementation("com.google.firebase:firebase-auth")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}