package com.example.lessons.utils.di

import android.content.Context
import com.example.lessons.di.provider.DataDependenciesProvider
import com.example.lessons.di.provider.ThemeDependenciesProvider

internal fun  Context.getThemeDependenciesProvider() =
    (applicationContext as ThemeDependenciesProvider).getThemeDependencies()