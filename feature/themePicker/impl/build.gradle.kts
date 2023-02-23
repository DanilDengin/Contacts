plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

android {
    compileSdk = AppConfig.compileSdkVersion
    namespace="com.example.themePicker.impl"
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
    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = AppConfig.javaVersion
    }
}

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
