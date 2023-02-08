package com.example.lessons.themePicker.presentation

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.lessons.di.themePicker.ThemeScope
import javax.inject.Inject

@ThemeScope
class ThemeDelegate @Inject constructor(private val sharedPreferences: SharedPreferences) {

    private fun getCurrentTheme() = sharedPreferences.getString(CURRENT_MODE, LIGHT_MODE)

    fun setThemeButton(
        setLightModeButton: () -> Unit,
        setNightModeButton: () -> Unit,
        setSystemModeButton: () -> Unit,
    ) {
        when (getCurrentTheme()) {
            LIGHT_MODE -> setLightModeButton()
            NIGHT_MODE -> setNightModeButton()
            SYSTEM_MODE -> setSystemModeButton()
        }
    }

    fun setTheme() {
        val themeMode = when (getCurrentTheme()) {
            LIGHT_MODE -> AppCompatDelegate.MODE_NIGHT_NO
            NIGHT_MODE -> AppCompatDelegate.MODE_NIGHT_YES
            SYSTEM_MODE -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            else -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(themeMode)
    }

    fun setNightMode() {
        updateTheme(NIGHT_MODE)
    }

    fun setLightMode() {
        updateTheme(LIGHT_MODE)
    }

    fun setSystemMode() {
        updateTheme(SYSTEM_MODE)
    }

    private fun updateTheme(mode: String) {
        sharedPreferences.edit().apply {
            putString(CURRENT_MODE, mode)
            apply()
        }
        setTheme()
    }

    private companion object {
        const val LIGHT_MODE = "lightMode"
        const val NIGHT_MODE = "nightMode"
        const val SYSTEM_MODE = "systemMode"
        const val CURRENT_MODE = "currentMode"
    }
}