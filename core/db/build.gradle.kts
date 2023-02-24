plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    room()
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
}