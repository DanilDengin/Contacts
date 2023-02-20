package com.example.impl.map.presentation

import androidx.lifecycle.ViewModel
import com.example.impl.map.di.DaggerMapComponent
import com.example.impl.map.di.MapExternalDependencies
import com.example.impl.map.presentation.MapComponentDependenciesProvider.featureDependencies
import com.example.utils.delegate.unsafeLazy

internal class MapComponentViewModel : ViewModel() {

    val component by unsafeLazy {
        DaggerMapComponent.factory()
            .create(checkNotNull(featureDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        featureDependencies = null
    }
}

internal object MapComponentDependenciesProvider {
    var featureDependencies: MapExternalDependencies? = null
}