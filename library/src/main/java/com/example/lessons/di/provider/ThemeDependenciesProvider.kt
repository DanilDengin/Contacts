package com.example.lessons.di.provider

import com.example.lessons.di.themePicker.ThemeComponent

interface ThemeDependenciesProvider {

    fun getThemeDependencies(): ThemeComponent
}