package com.example.lessons.themePicker.di

import com.example.lessons.di.theme.ThemeComponent
import com.example.lessons.themePicker.presentation.ThemePickerFragment
import dagger.Component

@ThemePickerScope
@Component(dependencies = [ThemeComponent::class])
internal interface ThemePickerComponent {

    fun inject(themePickerFragment: ThemePickerFragment)

    @Component.Factory
    interface Factory {
        fun create(themeComponent: ThemeComponent): ThemePickerComponent
    }
}
