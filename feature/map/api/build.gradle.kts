plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinParcelize)
    id(Plugins.jetbrainsAndroid)
    id(Plugins.kotlinKapt)
}

applyAndroid()

dependencies {
    implementation(project(":core:network"))
    implementation(Dependencies.cicerone)
}