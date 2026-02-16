import com.android.build.api.variant.BuildConfigField
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.ssavice.android.library)
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
    alias(libs.plugins.ssavice.hilt)
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.ssavice.core.mappicker"
}
val localProps =
    providers
        .fileContents(isolated.rootProject.projectDirectory.file("local.properties"))
        .asText
        .map {
            Properties().apply { load(it.reader()) }
        }

val kakaoRestKey =
    localProps
        .map { it.getProperty("KAKAO_REST_KEY") }
        .orElse("http://example.com")

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
    api(projects.core.data)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
}

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "KAKAO_REST_KEY",
            kakaoRestKey.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
