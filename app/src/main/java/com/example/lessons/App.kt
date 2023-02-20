package com.example.lessons

import android.app.Application
import com.example.lessons.di.AppComponent
import com.example.lessons.di.DaggerAppComponent
import com.example.utils.constans.MAPKIT_API_KEY
import com.github.terrakok.cicerone.Cicerone
import com.yandex.mapkit.MapKitFactory

class App : Application() {

    lateinit var appComponent: AppComponent
    private val cicerone = Cicerone.create()

    private val router = cicerone.router

    private val navigatorHolder = cicerone.getNavigatorHolder()

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        appComponent = DaggerAppComponent.factory()
            .create(this, router, navigatorHolder)
    }
}