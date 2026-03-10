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
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.ssavice.ssavice"

    defaultConfig {
        applicationId = "com.ssavice.ssavice"
        versionCode = 5
        versionName = "0.0.1"
    }
}

dependencies {
    implementation(projects.feature.userHome)
    implementation(projects.feature.search)
    implementation(projects.feature.searchResult)
    implementation(projects.feature.serviceDetail)
    implementation(projects.feature.sellerDetail)
    implementation(projects.feature.userMyPage)
    implementation(projects.feature.userMyService)
    implementation(projects.feature.editProfile)
    implementation(projects.feature.login)
    implementation(projects.feature.postReview)
    implementation(projects.feature.sellerReviews)
    implementation(projects.feature.userMain)
    implementation(projects.feature.userLiked)
    implementation(projects.feature.chat)
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
    implementation(projects.core.chat)

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
    implementation(platform(libs.firebase.bom))

    implementation(libs.androidx.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.kakao.map)
    implementation(libs.kakao.auth)
    androidTestImplementation(libs.androidx.espresso.core)
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

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "KAKAO_API_KEY",
            kakaoUserApiKey.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
