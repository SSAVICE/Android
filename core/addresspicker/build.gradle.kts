plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.hilt)
}

android {
    namespace = "com.ssavice.core.mappicker"
}

dependencies {
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    api(libs.androidx.webkit)

    api(projects.core.designsystem)
    api(projects.core.model)
}
