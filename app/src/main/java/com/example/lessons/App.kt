package com.example.lessons

import android.app.Application
import com.example.lessons.di.AppComponent
import com.example.lessons.di.AppModule
import com.example.lessons.di.DaggerAppComponent
import com.example.lessons.di.RepositoryModule

class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .repositoryModule(RepositoryModule())
            .build()
    }
}