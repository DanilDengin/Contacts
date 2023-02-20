package com.example.lessons.di

import android.content.Context
import android.content.SharedPreferences
import com.example.api.map.MapScreenApi
import com.example.db.database.ContactMapDatabase
import com.example.di.AppScope
import com.example.impl.contacts.di.ContactsExternalDependencies
import com.example.impl.contacts.domain.receiver.BirthdayReceiverProvider
import com.example.impl.map.di.MapExternalDependencies
import com.example.lessons.MainActivity
import com.example.themePicker.api.ThemeScreenApi
import com.example.themePicker.impl.di.ThemeExternalDependencies
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@AppScope
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DataModule::class,
        FeatureExternalDepsModule::class,
        ReceiverBindingModule::class
    ]
)
interface AppComponent : ContactsExternalDependencies, ThemeExternalDependencies, MapExternalDependencies
{
    override val router: Router
    override val context: Context
    override val themeApi: ThemeScreenApi
    override val mapApi: MapScreenApi
    override val birthdayReceiverProvider: BirthdayReceiverProvider
    override val sharedPref: SharedPreferences
    override val contactMapDatabase: ContactMapDatabase
    override val retrofit: Retrofit

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            @BindsInstance router: Router,
            @BindsInstance navigatorHolder: NavigatorHolder
        ): AppComponent
    }
}