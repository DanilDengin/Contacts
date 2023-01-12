package com.example.lessons

import android.app.Application
import com.example.lessons.di.AppComponent
import com.example.lessons.di.ContactComponentDependencies
import com.example.lessons.di.ContactComponentDependenciesProvider
import com.example.lessons.di.DaggerAppComponent

internal class App : Application(), ContactComponentDependenciesProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }

    override fun getContactComponentDependencies(): ContactComponentDependencies {
        return appComponent
    }
}