plugins {
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
}

android {
    namespace = "com.ssavice.feature.userchatting"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.chat)
    api(libs.androidx.paging.compose)
}
