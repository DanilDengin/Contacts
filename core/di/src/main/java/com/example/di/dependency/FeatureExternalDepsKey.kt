package com.example.di.dependency

import dagger.MapKey
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class FeatureExternalDepsKey(val value: KClass<out FeatureExternalDeps>)