package com.example.lessons.di

import com.example.contact.impl.di.ContactsScreenApiModule
import com.example.impl.map.di.MapScreenApiModule
import com.example.themePicker.impl.di.ThemeScreenApiModule
import dagger.Module

@Module(
    includes = [
        ContactsScreenApiModule::class,
        ThemeScreenApiModule::class,
        MapScreenApiModule::class
    ]
)
internal interface AppModule
