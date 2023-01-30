package com.example.lessons.utils.di

import android.content.Context
import com.example.lessons.di.provider.DiDependenciesProvider

fun <T> Context.getDependenciesProvider() where T : DiDependenciesProvider =
    (applicationContext as T).getDependencies()