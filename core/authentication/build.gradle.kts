plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.hilt)
}
android {
    namespace = "com.ssavice.core.authentication"
}

dependencies {
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.datastore)
    implementation(projects.core.model)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)

    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    implementation(libs.androidx.hilt.navigation.compose)
}
