package com.example.lessons.di.theme

import com.example.lessons.themePicker.presentation.ThemeDelegate
import dagger.Component

@ThemeScope
@Component(dependencies = [ThemeComponentDependencies::class])
interface ThemeComponent {

    fun getThemeDelegate(): ThemeDelegate

    @Component.Factory
    interface Factory {
        fun create(themeComponentDependencies: ThemeComponentDependencies): ThemeComponent
    }
}