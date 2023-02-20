package com.example.themePicker.impl.di

import com.example.di.FeatureScope
import com.example.themePicker.impl.presentation.ThemePickerFragment
import dagger.Component

@FeatureScope
@Component(dependencies = [ThemeExternalDependencies::class])
internal interface ThemeComponent {

    fun inject(themePickerFragment: ThemePickerFragment)

    @Component.Factory
    interface Factory {
        fun create(themeExternalDependencies: ThemeExternalDependencies): ThemeComponent
    }
}
