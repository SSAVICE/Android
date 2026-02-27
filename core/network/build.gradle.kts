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
    namespace = "com.ssavice.core.network"
}

val localProps =
    providers
        .fileContents(isolated.rootProject.projectDirectory.file("local.properties"))
        .asText
        .map {
            Properties().apply { load(it.reader()) }
        }

val backendURL =
    localProps
        .map { it.getProperty("BACKEND_URL") }
        .orElse("http://example.com")

val kakaoRestURL =
    localProps
        .map { it.getProperty("KAKAO_REST_URL") }
        .orElse("http://example.com")

val webSocketURL =
    localProps.map { it.getProperty("WEBSOCKET_URL") }.orElse("ws://example.com")


dependencies {
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(projects.core.datastore)
    api(projects.core.model)
}

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "BACKEND_URL",
            backendURL.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
        it.buildConfigFields!!.put(
            "KAKAO_REST_URL",
            kakaoRestURL.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
        it.buildConfigFields!!.put(
            "WEBSOCKET_URL",
            webSocketURL.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
