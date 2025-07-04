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
    //implementation(libs.androidx.lifecycle.runtime)
    //implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.android.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.runtime)


    implementation(libs.androidx.fragment.v162)
    implementation(project(":observables"))
    //implementation(libs.androidx.room.common)


    // Unit Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.test.ext.espresso.core)
    testImplementation("androidx.room:room-testing:2.6.1")
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)

    // Instrumented Testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.espresso.core)
    androidTestImplementation(libs.androidx.core)
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.0.0")


    // Room Annotation Processor
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    /*
    implementation(project(":lib"))

    implementation(libs.android.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.room:room-runtime:2.6.1")


    implementation("androidx.fragment:fragment:1.6.2")
    //implementation(libs.androidx.fragment)
    implementation(project(":observables"))

    //testImplementation(libs.junit4)
    testImplementation(libs.androidx.core)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.androidx.test.ext.junit)
    testImplementation(libs.androidx.test.ext.espresso.core)
    testImplementation("junit:junit:4.13.2")

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.ext.espresso.core)
    androidTestImplementation(libs.androidx.core)

//    androidTestImplementation("androidx.test:runner:1.6.2")
//    androidTestImplementation("androidx.test:rules:1.6.1u")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

     */

}