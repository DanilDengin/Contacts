plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

android {
    compileSdk = AppConfig.compileSdkVersion
    namespace="com.example.impl.map"
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
    implementation(project(":core:network"))
    implementation(project(":core:db"))
    implementation(project(":feature:map:api"))
    implementation(Dependencies.cicerone)
    dagger()
    room()
    coroutines()
    retrofit()
    implementation(Dependencies.appCompat)
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.lifecycleViewModel)
    implementation(Dependencies.lifecycleLiveData)
    implementation(Dependencies.lifecycleRuntime)
    implementation(Dependencies.viewBindingDelegate)
    implementation(Dependencies.material)
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.yandexMap)
    implementation(Dependencies.constraint)
}