plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinParcelize)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
    id(Plugins.junit) version Versions.junitPlugin
}

applyAndroid(useViewBinding = true)

dependencies {
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
    implementation(project(":core:di"))
    implementation(project(":core:mvvm"))
    implementation(project(":feature:contact:api"))
    implementation(project(":feature:themePicker:api"))
    implementation(project(":feature:map:api"))
    implementation(Dependencies.cicerone)
    dagger()
    coroutines()
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.viewBindingDelegate)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.lifecycleLiveData)
    implementation(Dependencies.lifecycleRuntime)
    testImplementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.material)
    implementation(Dependencies.mockk)
    implementation(Dependencies.material)
    testImplementation(Dependencies.logger)
    testImplementation(Dependencies.junitApi)
    testRuntimeOnly(Dependencies.junitEngine)
    testImplementation(Dependencies.coroutinesTest)
    implementation(Dependencies.espressoIdlingResource)
    androidTestImplementation(Dependencies.espresso)
}

