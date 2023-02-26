plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

applyAndroid()

dependencies {
    implementation(project(":core:db"))
    implementation(project(":core:di"))
    coroutines()
    dagger()
    room()
}