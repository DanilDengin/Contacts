// Top-level build file where you can add configuration options common to all sub-projects/modules.
//plugins {
//    id(Plugins.application) version "7.3.1" apply false
//    id(Plugins.androidLibrary) version "7.3.1" apply false
//    id(Plugins.jetbrainsAndroid) version "1.7.10" apply false
//    id(Plugins.jetbrainsJvm) version "1.7.20" apply false
//}

buildscript {
    repositories{
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}