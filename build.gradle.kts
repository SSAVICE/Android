// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.ssavice.root) apply false
    alias(libs.plugins.ktlint)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    ktlint {
        version.set("1.8.0")
    }
}

tasks.register("ktlintAll") {
    group = "verification"
    description = "Runs ktlint for main build and included builds (build-logic)."

    dependsOn(gradle.includedBuilds.mapNotNull { null })
    dependsOn(gradle.rootProject.tasks.matching { it.name == "ktlintCheck" })
    dependsOn(gradle.includedBuild("build-logic").task(":convention:ktlintCheck"))
}

tasks.register("ktlintFormatAll") {
    group = "formatting"
    description = "Runs ktlintFormat for main build and build-logic."

    dependsOn(gradle.rootProject.tasks.matching { it.name == "ktlintFormat" })
    dependsOn(gradle.includedBuild("build-logic").task(":convention:ktlintFormat"))
}
