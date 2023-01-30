package com.example.lessons

import android.app.Application
import com.example.lessons.di.AppComponent
import com.example.lessons.di.DaggerAppComponent
import com.example.lessons.di.provider.DiDependencies
import com.example.lessons.di.provider.DiDependenciesProvider
import com.example.lessons.utils.constans.MAPKIT_API_KEY
import com.yandex.mapkit.MapKitFactory

internal class App : Application(), DiDependenciesProvider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }

    override fun getDependencies(): DiDependencies {
        return appComponent
    }
}