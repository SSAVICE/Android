plugins {
    alias(libs.plugins.ssavice.android.feature)
    alias(libs.plugins.ssavice.android.library.compose)
}

android {
    namespace = "com.ssavice.feature.servicedetail"
}

dependencies {
    implementation(projects.core.data)
    implementation(libs.coil.kt.compose)
}
