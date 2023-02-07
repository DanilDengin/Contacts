package com.example.lessons.utils.di

import android.content.Context
import com.example.lessons.di.provider.DataDependenciesProvider

internal fun <T> Context.getDataDependenciesProvider() =
    (applicationContext as DataDependenciesProvider).getDataDependencies() as T