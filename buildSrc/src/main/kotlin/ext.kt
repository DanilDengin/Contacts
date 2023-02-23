import org.gradle.api.Project

fun Project.applyAndroid(
    useViewBinding: Boolean = false,
    generateBuildConfig: Boolean = false
) {
//    extensions.findByType<LibraryExtension>()?.apply {
//        compileSdk = AppConfig.compileSdkVersion
//        buildToolsVersion = AppConfig.buildToolVersion
//
//        defaultConfig {
//            targetSdk = AppConfig.targetSdkVersion
//            minSdk = AppConfig.minSdkVersion
//        }
//
//        buildFeatures {
//            viewBinding = useViewBinding
//            buildConfig = generateBuildConfig
//        }
//
//        lint {
//            isCheckReleaseBuilds = false
//            isAbortOnError = true
//            isIgnoreWarnings = true
//            isQuiet = true
//        }
//
//        packagingOptions {
//            resources.excludes.addAll(
//                setOf(
//                    "META-INF/DEPENDENCIES",
//                    "META-INF/LICENSE",
//                    "META-INF/LICENSE.txt",
//                    "META-INF/license.txt",
//                    "META-INF/NOTICE",
//                    "META-INF/NOTICE.txt",
//                    "META-INF/notice.txt",
//                    "META-INF/ASL2.0",
//                    "META-INF/AL2.0",
//                    "META-INF/LGPL2.1",
//                )
//            )
//        }
//
//        compileOptions {
//            isCoreLibraryDesugaringEnabled = true
//            sourceCompatibility = JavaVersion.VERSION_11
//            targetCompatibility = JavaVersion.VERSION_11
//        }
//    }
//
//    extensions.findByType<KotlinCompile>()?.apply {
//        kotlinOptions {
//            jvmTarget = AppConfig.javaVersion
//            freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
//        }
//    }
//
//    dependencies {
//        add("implementation", Dependencies.kotlinStdLib)
//    }
}