import com.android.build.api.variant.BuildConfigField
import java.util.Properties

plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.hilt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.ssavice.core.room"
}

dependencies {
    ksp(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.room.runtime)
    api(libs.androidx.paging.common)
    api(projects.core.model)
}
