plugins {
    alias(libs.plugins.ssavice.android.application)
    alias(libs.plugins.ssavice.hilt)
    alias(libs.plugins.ssavice.android.application.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.ssavice_seller"

    defaultConfig {
        applicationId = "com.ssavice_seller"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.feature.sellerRegister)
    implementation(projects.feature.sellerMain)
    implementation(projects.feature.postService)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.model)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive)
    implementation(libs.androidx.compose.material3.adaptive.layout)
    implementation(libs.androidx.compose.material3.adaptive.navigation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime.tracing)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
