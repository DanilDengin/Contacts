plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid(useViewBinding = true)

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
    implementation(project(":core:di"))
    implementation(project(":core:mvvm"))
    implementation(project(":feature:themePicker:api"))

    dagger()
    implementation(Dependencies.viewBindingDelegate)
    implementation(Dependencies.cicerone)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.coreKtx)
}
