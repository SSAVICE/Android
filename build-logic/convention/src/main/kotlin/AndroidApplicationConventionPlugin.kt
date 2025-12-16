import com.android.build.api.dsl.ApplicationExtension
import com.ssavice.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.application")
            apply(plugin = "org.jetbrains.kotlin.android")

            extensions.getByType<ApplicationExtension>().apply {
                configureKotlinAndroid(this)
                defaultConfig {
                    minSdk = 27
                    targetSdk = 36
                    @Suppress("UnstableApiUsage")
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                buildTypes {
                    release {
                        isMinifyEnabled = false
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro",
                        )
                    }
                }
            }
        }
    }
}
