import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

fun Project.applyAndroid(
    useViewBinding: Boolean = false,
) {
    extensions.findByType<LibraryExtension>()?.apply {

        compileSdk = AppConfig.compileSdkVersion
        defaultConfig {
            minSdk = AppConfig.minSdkVersion
            targetSdk = AppConfig.targetSdkVersion
            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        packagingOptions {
            resources.excludes.add("META-INF/*")
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }
        buildFeatures {
            viewBinding = useViewBinding
        }
        extensions.findByType<KotlinCompile>()?.apply {
            kotlinOptions {
                jvmTarget = AppConfig.javaVersion
            }
        }
    }
}