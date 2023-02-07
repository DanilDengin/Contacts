package com.example.lessons.utils.di

import android.content.Context
import com.example.lessons.di.provider.AppDependenciesProvider

internal fun <T> Context.getAppDependenciesProvider()  =
    (applicationContext as AppDependenciesProvider).getAppDependencies() as T