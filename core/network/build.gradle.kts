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
        .fileContents(rootProject.layout.projectDirectory.file("local.properties"))
        .asText
        .map {
            Properties().apply { load(it.reader()) }
        }

val backendURL =
    localProps
        .map { it.getProperty("BACKEND_URL") }
        .orElse("http://example.com")

dependencies {
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
}

androidComponents {
    onVariants {
        it.buildConfigFields!!.put(
            "BACKEND_URL",
            backendURL.map { value ->
                BuildConfigField(type = "String", value = """"$value"""", comment = null)
            },
        )
    }
}
