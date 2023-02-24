plugins {
    id(Plugins.androidLibrary)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    implementation(project(":core:utils"))

    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.material)
}
