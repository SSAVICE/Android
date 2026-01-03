plugins {
    alias(libs.plugins.ssavice.jvm.library)
    alias(libs.plugins.ssavice.hilt)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
