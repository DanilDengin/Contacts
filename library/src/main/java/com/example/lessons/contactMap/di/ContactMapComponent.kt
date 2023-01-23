package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.presentation.ContactMapFragment
import com.example.lessons.di.ContactComponentDependencies
import dagger.Component

@ContactMapScope
@Component(
    dependencies = [MapComponentDependencies::class],
    modules = [ContactMapModule::class]
)
internal interface ContactMapComponent {
    fun inject(mapFragment: ContactMapFragment)
}