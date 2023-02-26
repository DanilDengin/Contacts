package com.example.impl.map.di

import com.example.common.address.di.ContactMapRepositoryBinding
import com.example.di.FeatureScope
import com.example.impl.map.presentation.contactMap.ContactMapFragment
import com.example.impl.map.presentation.contactMapRoutePicker.ContactMapRoutePickerFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [MapModule::class, ContactMapRepositoryBinding::class],
    dependencies = [MapExternalDependencies::class]
)
internal interface MapComponent {

    fun inject(mapFragment: ContactMapFragment)

    fun inject(mapPickerFragment: ContactMapRoutePickerFragment)

    @Component.Factory
    interface Factory {
        fun create(mapExternalDependencies: MapExternalDependencies): MapComponent
    }
}