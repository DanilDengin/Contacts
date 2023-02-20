package com.example.themePicker.impl.di

import android.content.SharedPreferences
import com.example.di.dependency.FeatureExternalDeps

interface ThemeExternalDependencies : FeatureExternalDeps {

    val sharedPref: SharedPreferences
}