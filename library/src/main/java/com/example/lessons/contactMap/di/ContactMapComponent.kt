package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.presentation.ContactMapFragment
import com.example.lessons.contactMapPicker.presentation.ContactMapPickerFragment
import com.example.lessons.di.contactMap.MapComponentDependencies
import dagger.Component

@ContactMapScope
@Component(
    dependencies = [MapComponentDependencies::class],
    modules = [ContactMapModule::class]
)
internal interface ContactMapComponent {

    fun inject(mapFragment: ContactMapFragment)

    fun inject(mapPickerFragment: ContactMapPickerFragment)

    @Component.Factory
    interface Factory {
        fun create(mapComponentDependencies: MapComponentDependencies): ContactMapComponent
    }
}