package com.example.lessons.di

import android.content.SharedPreferences
import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import com.example.lessons.di.contactMap.MapComponentDependencies
import com.example.lessons.di.themePicker.ThemePickerComponentDependencies
import dagger.Component
import retrofit2.Retrofit

@DataScope
@Component(
    modules = [DataModule::class],
    dependencies = [AppComponent::class],
)
internal interface DataComponent : ThemePickerComponentDependencies, MapComponentDependencies {

    override fun getSharedPref(): SharedPreferences

    override fun getContactDatabase(): ContactMapDatabase

    override fun getRetrofit(): Retrofit

}