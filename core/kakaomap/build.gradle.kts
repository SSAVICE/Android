import com.android.build.api.variant.BuildConfigField
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.ssavice.android.library.compose)
    alias(libs.plugins.ssavice.android.feature)
}

android {
    buildFeatures {
        buildConfig = true
    }

    namespace = "com.ssavice.core.kakaomap"
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
    api(projects.core.data)

    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kakao.map)
}

val localProps =
    providers
        .fileContents(isolated.rootProject.projectDirectory.file("local.properties"))
        .asText
        .map {
            Properties().apply { load(it.reader()) }
        }

val kakaoUserApiKey =
    localProps
        .map { it.getProperty("KAKAO_API_KEY") }
        .orElse("0")

val kakaoSellerApiKey =
    localProps
        .map { it.getProperty("KAKAO_API_KEY_SELLER") }
        .orElse("1")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "KAKAO_API_KEY",
            kakaoUserApiKey.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )

        it.buildConfigFields!!.put(
            "KAKAO_API_KEY_SELLER",
            kakaoSellerApiKey.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
