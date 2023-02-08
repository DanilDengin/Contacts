package com.example.lessons.di.themePicker

import com.example.lessons.themePicker.presentation.ThemeDelegate
import dagger.Component

@ThemeScope
@Component(dependencies = [ThemeComponentDependencies::class])
interface ThemeComponent {

    fun getThemeDelegate(): ThemeDelegate
}