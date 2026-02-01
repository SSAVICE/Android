plugins {
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
}

android {
    namespace = "com.ssavice.feature.userhome"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.addresspicker)
}
