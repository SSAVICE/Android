plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.hilt)
}
android {
    namespace = "com.ssavice.core.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.network)
    implementation(projects.core.model)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
}
