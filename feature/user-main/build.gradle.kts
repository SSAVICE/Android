plugins {
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
}

android {
    namespace = "com.ssavice.feature.usermain"
}

dependencies {
    implementation(projects.core.data)
}
