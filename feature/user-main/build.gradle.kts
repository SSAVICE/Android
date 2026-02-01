plugins {
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
}

android {
    namespace = "com.ssavice.feature.usermain"
}

dependencies {
    implementation(projects.feature.userHome)
    implementation(projects.feature.userMyPage)
    implementation(projects.feature.userChatting)
}
