import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.dagger() {
    implementation(Dependencies.dagger)
    kapt(Dependencies.daggerKapt)
}

fun DependencyHandlerScope.retrofit() {
    implementation(Dependencies.retrofit)
    implementation(Dependencies.gsonRetrofitConvertor)
    implementation(Dependencies.http)
    implementation(Dependencies.httpLoggingInterceptor)
}

fun DependencyHandlerScope.room() {
    implementation(Dependencies.room)
    implementation(Dependencies.roomKtx)
    kapt(Dependencies.roomKapt)
    annotationProcessor(Dependencies.roomKapt)
}

fun DependencyHandlerScope.coroutines() {
    implementation(Dependencies.coroutines)
    implementation(Dependencies.coroutinesCore)
}

private fun DependencyHandlerScope.implementation(name: String) {
    add("implementation", name)
}

private fun DependencyHandlerScope.kapt(name: String) {
    add("kapt", name)
}

private fun DependencyHandlerScope.annotationProcessor(name: String) {
    add("annotationProcessor", name)
}