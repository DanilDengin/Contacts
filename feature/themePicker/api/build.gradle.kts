plugins {
    id(Plugins.androidLibrary)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    implementation(Dependencies.cicerone)
}
