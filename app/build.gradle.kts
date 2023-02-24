plugins {
    id(Plugins.application)
    id(Plugins.kotlinKapt)
    id(Plugins.kotlinAndroid)
}

android {
    compileSdk = AppConfig.compileSdkVersion

    defaultConfig {
        applicationId = AppConfig.applicationId
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        versionCode = AppConfig.versionCode
        versionName = AppConfig.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ui"))
    implementation(project(":core:utils"))
    implementation(project(":core:di"))
    implementation(project(":core:db"))
    implementation(project(":feature:contact:api"))
    implementation(project(":feature:contact:impl"))
    implementation(project(":feature:themePicker:api"))
    implementation(project(":feature:themePicker:impl"))
    implementation(project(":feature:map:api"))
    implementation(project(":feature:map:impl"))

    implementation(Dependencies.appCompat)
    dagger()
    implementation(Dependencies.cicerone)
    retrofit()
    room()
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.activityKtx)
    implementation(Dependencies.yandexMap)
}