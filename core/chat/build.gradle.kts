plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.hilt)
    alias(libs.plugins.kotlin.serialization)
}
android {
    namespace = "com.ssavice.core.chat"
}

dependencies {
    api(projects.core.common)
    api(projects.core.network)
    api(projects.core.datastore)
    api(projects.core.room)
    implementation(projects.core.model)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.common)
    implementation(libs.room.runtime)
    annotationProcessor(libs.room.compiler)

    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
}
