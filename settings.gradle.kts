pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Ssavice"
include(":app")
include(":core:data")
include(":core:domain")
include(":core:network")
include(":core:ui")
include(":core:model")
include(":core:common")
include(":feature:login")
include(":core:designsystem")
include(":feature:seller-register")
include(":ssavice-seller")
include(":feature:post-service")
include(":feature:seller-main")
include(":feature:user-main")
