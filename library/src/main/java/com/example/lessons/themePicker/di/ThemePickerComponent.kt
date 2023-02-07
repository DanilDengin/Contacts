package com.example.lessons.themePicker.di

import com.example.lessons.di.themePicker.ThemePickerComponentDependencies
import com.example.lessons.themePicker.presentation.ThemePickerFragment
import dagger.Component

@ThemePickerScope
@Component(dependencies = [ThemePickerComponentDependencies::class])
interface ThemePickerComponent {

    fun inject(themePickerFragment: ThemePickerFragment)
}