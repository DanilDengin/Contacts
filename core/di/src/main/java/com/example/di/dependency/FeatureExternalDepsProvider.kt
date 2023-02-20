package com.example.di.dependency

import androidx.fragment.app.Fragment

typealias FeatureExternalDepsContainer = Map<Class<out FeatureExternalDeps>, @JvmSuppressWildcards FeatureExternalDeps>

interface FeatureExternalDepsProvider {
    val deps: FeatureExternalDepsContainer
}

inline fun <reified T : FeatureExternalDeps> FeatureExternalDepsProvider.get(): T =
    deps.getValue(T::class.java) as T

inline fun <reified T : FeatureExternalDeps> Fragment.findFeatureExternalDeps(): T =
    (requireActivity() as? FeatureExternalDepsProvider)?.get() ?: error("Feature external deps not found")