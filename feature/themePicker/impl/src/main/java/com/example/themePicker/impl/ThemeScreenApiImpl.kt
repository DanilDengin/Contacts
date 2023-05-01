package com.example.themePicker.impl

import com.example.themePicker.api.ThemeScreenApi
import com.example.themePicker.impl.presentation.ThemePickerFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

class ThemeScreenApiImpl @Inject constructor() : ThemeScreenApi {
    override fun getThemeScreen() = FragmentScreen { ThemePickerFragment() }
}
