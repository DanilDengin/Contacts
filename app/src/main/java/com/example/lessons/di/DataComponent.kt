package com.example.lessons.di

import android.content.Context
import android.content.SharedPreferences
import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import com.example.lessons.di.contactMap.MapComponentDependencies
import com.example.lessons.di.themePicker.ThemeComponentDependencies
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit

@DataScope
@Component(
    modules = [DataModule::class],
    dependencies = [AppComponent::class],
)
internal interface DataComponent : ThemeComponentDependencies, MapComponentDependencies {

    override fun getSharedPref(): SharedPreferences

    override fun getContactDatabase(): ContactMapDatabase

    override fun getRetrofit(): Retrofit

    @Component.Factory
    interface Factory {
        fun create(appComponent: AppComponent): DataComponent
    }
}