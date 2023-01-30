package com.example.lessons.di.contactMap

import com.example.lessons.di.provider.DiDependenciesProvider

internal interface MapComponentDependenciesProvider : DiDependenciesProvider {

    override fun getDependencies(): MapComponentDependencies
}