package com.example.lessons.di.provider

import com.example.lessons.di.theme.ThemeComponent

interface ThemeDependenciesProvider {

    fun getThemeDependencies(): ThemeComponent
}