plugins {
    id("java-library")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // This library contains no executable code, so it is
    // safe to use even in a non-Android library module.
    implementation(libs.androidx.annotations)
    implementation(libs.androidx.lifecycle.common.jvm)
    implementation(project(":observables"))

    testImplementation(libs.junit4)
    testImplementation(libs.hamcrest)
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.junit.jupiter)

}