plugins {
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
}

android {
    namespace = "com.ssavice.feature.sellermain"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.feature.sellerHome)
    implementation(projects.feature.chatList)
    implementation(projects.feature.sellerMyPage)
}
