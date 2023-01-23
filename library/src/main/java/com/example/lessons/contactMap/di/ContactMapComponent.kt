package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.presentation.ContactMapFragment
import dagger.Component

@ContactMapScope
@Component(
    dependencies = [MapComponentDependencies::class],
    modules = [ContactMapModule::class]
)
internal interface ContactMapComponent {

    fun inject(mapFragment: ContactMapFragment)

}