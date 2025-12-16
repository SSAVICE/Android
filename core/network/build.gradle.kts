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

val consumerTokens =
    List(2) { i ->
        localProps
            .map { it.getProperty("TEST_CUSTOMER_KEY$i") }
            .orElse("NOT_FOUND")
    }

val sellerTokens =
    List(2) { i ->
        localProps
            .map { it.getProperty("TEST_CUSTOMER_KEY$i") }
            .orElse("NOT_FOUND")
    }

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

        consumerTokens.forEachIndexed { i, token ->
            it.buildConfigFields!!.put(
                "CONSUMER_TOKEN$i",
                token.map { value ->
                    BuildConfigField(type = "String", value = """"$value"""", comment = null)
                },
            )
        }

        consumerTokens.forEachIndexed { i, token ->
            it.buildConfigFields!!.put(
                "SELLER_TOKEN$i",
                token.map { value ->
                    BuildConfigField(type = "String", value = """"$value"""", comment = null)
                },
            )
        }
    }
}
