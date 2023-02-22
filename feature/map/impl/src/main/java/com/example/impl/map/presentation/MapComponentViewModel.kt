package com.example.impl.map.presentation

import androidx.lifecycle.ViewModel
import com.example.impl.map.di.DaggerMapComponent
import com.example.impl.map.di.MapExternalDependencies
import com.example.impl.map.presentation.MapComponentDependenciesProvider.mapExternalDependencies
import com.example.utils.delegate.unsafeLazy

internal class MapComponentViewModel : ViewModel() {

    val mapComponent by unsafeLazy {
        DaggerMapComponent.factory()
            .create(checkNotNull(mapExternalDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        mapExternalDependencies = null
    }
}

internal object MapComponentDependenciesProvider {
    var mapExternalDependencies: MapExternalDependencies? = null
}