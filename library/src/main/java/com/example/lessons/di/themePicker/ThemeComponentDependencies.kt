package com.example.lessons.di.themePicker

import android.content.SharedPreferences
import com.example.lessons.di.provider.DataDependencies

interface ThemeComponentDependencies : DataDependencies {

    fun getSharedPref(): SharedPreferences
}