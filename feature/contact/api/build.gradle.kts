plugins {
    id(Plugins.androidLibrary)
    id(Plugins.jetbrainsAndroid)
}

android {
    compileSdk = AppConfig.compileSdkVersion
    namespace="com.example.contact.api"
    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = AppConfig.javaVersion
    }
}

dependencies {
    implementation(Dependencies.cicerone)
}
