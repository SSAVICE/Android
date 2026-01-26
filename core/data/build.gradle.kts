plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.hilt)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.ssavice.core.data"
}

dependencies {
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.datastore)
    implementation(projects.core.model)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
}
