package com.example.lessons.di.theme

import android.content.SharedPreferences
import com.example.lessons.di.provider.DataDependencies

interface ThemeComponentDependencies : DataDependencies {

    fun getSharedPref(): SharedPreferences
}