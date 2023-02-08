package com.example.lessons

import android.app.Application
import com.example.lessons.di.AppComponent
import com.example.lessons.di.DaggerAppComponent
import com.example.lessons.di.DaggerDataComponent
import com.example.lessons.di.DataComponent
import com.example.lessons.di.provider.AppDependencies
import com.example.lessons.di.provider.AppDependenciesProvider
import com.example.lessons.di.provider.DataDependencies
import com.example.lessons.di.provider.DataDependenciesProvider
import com.example.lessons.di.provider.ThemeDependenciesProvider
import com.example.lessons.di.themePicker.DaggerThemeComponent
import com.example.lessons.di.themePicker.ThemeComponent
import com.example.lessons.utils.constans.MAPKIT_API_KEY
import com.yandex.mapkit.MapKitFactory

internal class App : Application(), AppDependenciesProvider, DataDependenciesProvider,
    ThemeDependenciesProvider {

    private lateinit var appComponent: AppComponent

    private lateinit var dataComponent: DataComponent

    private lateinit var themeComponent: ThemeComponent

    override fun onCreate() {
        super.onCreate()

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        appComponent = DaggerAppComponent.factory()
            .create(this)

        dataComponent = DaggerDataComponent.factory()
            .create(appComponent)

        themeComponent = DaggerThemeComponent.factory()
            .create(dataComponent)
    }

    override fun getAppDependencies(): AppDependencies {
        return appComponent
    }

    override fun getDataDependencies(): DataDependencies {
        return dataComponent
    }

    override fun getThemeDependencies(): ThemeComponent {
        return themeComponent
    }
}