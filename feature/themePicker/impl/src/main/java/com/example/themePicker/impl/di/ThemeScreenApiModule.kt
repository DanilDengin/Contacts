package com.example.themePicker.impl.di

import com.example.themePicker.api.ThemeScreenApi
import com.example.themePicker.impl.ThemeScreenApiImpl
import dagger.Binds
import dagger.Module

@Module
interface ThemeScreenApiModule {
    @Binds
    fun bindThemeScreenApi(themeScreenApiImpl: ThemeScreenApiImpl): ThemeScreenApi
}