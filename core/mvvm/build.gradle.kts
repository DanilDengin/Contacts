plugins {
    id(Plugins.androidLibrary)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
}
