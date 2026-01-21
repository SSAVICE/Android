plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.hilt)
}
android {
    namespace = "com.ssavice.datastore"
}

dependencies {
    api(projects.core.common)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.model)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore)
    implementation(libs.tink)
}
