package com.example.lessons.utils.di

import android.content.Context
import com.example.lessons.di.ContactComponentDependenciesProvider

internal fun Context.getComponentDependencies() =
    (applicationContext as ContactComponentDependenciesProvider)
        .getContactComponentDependencies()