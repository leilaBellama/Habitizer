plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "edu.ucsd.cse110.habitizer.app"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "edu.ucsd.cse110.habitizer.app"
        minSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(project(":lib"))

    implementation(libs.android.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(project(":observables"))

    testImplementation(libs.junit4)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.test.ext.espresso.core)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.espresso.core)
    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test:runner:1.6.2")
//    androidTestImplementation("androidx.test:rules:1.6.1u")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
}