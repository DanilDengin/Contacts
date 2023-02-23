plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinParcelize)
    id(Plugins.kotlinKapt)
    id(Plugins.jetbrainsAndroid)
}

android {
    compileSdk = AppConfig.compileSdkVersion
    namespace="com.example.contact.impl"
    defaultConfig {
        minSdk = AppConfig.minSdkVersion
        targetSdk = AppConfig.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        resources.excludes.add("META-INF/*")
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
//    implementation(Dependencies.mockk)
    implementation(Dependencies.material)
//    testImplementation group: 'org.slf4j', name: 'slf4j-log4j12', version: '1.2'
    testImplementation(Dependencies.coroutinesTest)
//    androidTestImplementation(Dependencies.junitTest)
//    testImplementation(Dependencies.jupiter)
//    testImplementation(Dependencies.junit)
    implementation(Dependencies.espressoIdlingResource)
    androidTestImplementation(Dependencies.espresso)
}


//test() {
//    useJUnitPlatform()
//}

