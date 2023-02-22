package com.example.themePicker.impl.presentation

import androidx.lifecycle.ViewModel
import com.example.themePicker.impl.di.DaggerThemeComponent
import com.example.themePicker.impl.di.ThemeExternalDependencies
import com.example.themePicker.impl.presentation.ThemeFeatureComponentDependenciesProvider.themeExternalDependencies
import com.example.utils.delegate.unsafeLazy

internal class ThemeComponentViewModel : ViewModel() {
    val themeComponent by unsafeLazy {
        DaggerThemeComponent.factory()
            .create(checkNotNull(themeExternalDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        themeExternalDependencies = null
    }
}

internal object ThemeFeatureComponentDependenciesProvider {
    var themeExternalDependencies: ThemeExternalDependencies? = null
}