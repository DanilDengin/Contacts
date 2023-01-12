package com.example.lessons.utils

import android.content.Context
import com.example.lessons.di.ContactComponentDependenciesProvider

fun  Context.getComponentDependencies() =
    (applicationContext as ContactComponentDependenciesProvider)
        .getContactComponentDependencies()