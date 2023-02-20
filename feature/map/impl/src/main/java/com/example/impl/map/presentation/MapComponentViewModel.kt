package com.example.impl.map.presentation

import androidx.lifecycle.ViewModel
import com.example.impl.map.di.DaggerMapComponent
import com.example.impl.map.di.MapExternalDependencies
import com.example.impl.map.presentation.MapComponentDependenciesProvider.featureDependencies
import com.example.lessons.utils.delegate.unsafeLazy

class MapComponentViewModel: ViewModel() {

    val component by unsafeLazy {
        DaggerMapComponent.factory()
            .create(checkNotNull(featureDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        featureDependencies = null
    }
}

object MapComponentDependenciesProvider {
    var featureDependencies: MapExternalDependencies? = null
}