plugins {
    id(Plugins.androidLibrary)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    implementation(project(":core:utils"))
    retrofit()
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
}
