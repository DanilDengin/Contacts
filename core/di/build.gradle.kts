plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    dagger()
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
}