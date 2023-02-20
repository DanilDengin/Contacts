package com.example.lessons.di

import com.example.impl.contacts.di.ContactsScreenApiModule
import com.example.impl.map.di.MapScreenApiModule
import com.example.themePicker.impl.di.ThemeScreenApiModule
import dagger.Module

@Module
internal interface AppModule : ContactsScreenApiModule, ThemeScreenApiModule, MapScreenApiModule