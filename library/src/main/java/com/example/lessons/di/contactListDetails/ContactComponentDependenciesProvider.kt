package com.example.lessons.di.contactListDetails

import com.example.lessons.di.provider.DiDependenciesProvider

internal interface ContactComponentDependenciesProvider : DiDependenciesProvider {

    override fun getDependencies(): ContactComponentDependencies
}