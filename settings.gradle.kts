pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "lessons"
include (":app")
include (":core:network")
include (":core:mvvm")
include (":core:ui")
include (":core:utils")
include (":core:db")
include (":core:di")
include (":feature:contact:impl")
include (":feature:contact:api")
include (":feature:map:impl")
include (":feature:map:api")
include (":feature:themePicker:impl")
include (":feature:themePicker:api")
include (":feature:contact:impl")
include (":feature:contact:api")
