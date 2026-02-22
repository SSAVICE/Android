import com.android.build.api.variant.BuildConfigField
import java.util.Properties
import kotlin.apply

plugins {
    alias(libs.plugins.ssavice.android.application)
    alias(libs.plugins.ssavice.hilt)
    alias(libs.plugins.ssavice.android.application.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose)
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.distribution)
}

android {
    namespace = "com.ssavice_seller"

    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        applicationId = "com.ssavice_seller"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.feature.sellerRegister)
    implementation(projects.feature.sellerHome)
    implementation(projects.feature.sellerMain)
    implementation(projects.feature.sellerMyPage)
    implementation(projects.feature.postService)
    implementation(projects.feature.login)
    implementation(projects.feature.sellerMyService)
    implementation(projects.feature.sellerEditProfile)
    implementation(projects.feature.serviceDetail)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
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
    implementation(libs.firebase.bom)

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    implementation(libs.kakao.map)
    implementation(libs.kakao.auth)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

val localProps =
    providers
        .fileContents(isolated.rootProject.projectDirectory.file("local.properties"))
        .asText
        .map {
            Properties().apply { load(it.reader()) }
        }

val kakaoSellerApiKey =
    localProps
        .map { it.getProperty("KAKAO_API_KEY_SELLER") }
        .orElse("1")

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "KAKAO_API_KEY_SELLER",
            kakaoSellerApiKey.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
