package com.example.themePicker.impl.presentation

import androidx.lifecycle.ViewModel
import com.example.themePicker.impl.di.DaggerThemeComponent
import com.example.themePicker.impl.di.ThemeExternalDependencies
import com.example.themePicker.impl.presentation.ThemeFeatureComponentDependenciesProvider.featureDependencies
import com.example.utils.delegate.unsafeLazy

class ThemeComponentViewModel : ViewModel() {
    val component by unsafeLazy {
        DaggerThemeComponent.factory()
            .create(checkNotNull(featureDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        featureDependencies = null
    }
}

object ThemeFeatureComponentDependenciesProvider {
    var featureDependencies: ThemeExternalDependencies? = null
}