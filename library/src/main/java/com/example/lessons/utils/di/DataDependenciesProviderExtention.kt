package com.example.lessons.utils.di

import android.content.Context
import com.example.lessons.di.provider.DataDependenciesProvider

fun <T> Context.getDataDependenciesProvider() =
    (applicationContext as DataDependenciesProvider).getDataDependencies() as T