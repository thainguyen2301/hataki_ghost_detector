pluginManagement {
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
        maven("https://artifact.bytedance.com/repository/pangle/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven("https://artifact.bytedance.com/repository/pangle/")
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "H011-TNT-GHOST"
include(":app")
 