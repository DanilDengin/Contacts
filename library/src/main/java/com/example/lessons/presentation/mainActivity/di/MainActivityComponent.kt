package com.example.lessons.presentation.mainActivity.di

import com.example.lessons.di.themePicker.ThemePickerComponentDependencies
import com.example.lessons.presentation.mainActivity.MainActivity
import dagger.Component

@MainActivityScope
@Component(dependencies = [ThemePickerComponentDependencies::class])
interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)
}