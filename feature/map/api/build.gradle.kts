plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinParcelize)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    implementation(Dependencies.cicerone)
}