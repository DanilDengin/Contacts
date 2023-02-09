package com.example.lessons.presentation.mainActivity.di

import com.example.lessons.di.theme.ThemeComponent
import com.example.lessons.presentation.mainActivity.MainActivity
import dagger.Component

@MainActivityScope
@Component(dependencies = [ThemeComponent::class])
internal interface MainActivityComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(themeComponent: ThemeComponent): MainActivityComponent
    }
}