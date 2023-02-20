package com.example.impl.contacts.di

import android.content.Context
import com.example.api.map.MapScreenApi
import com.example.di.dependency.FeatureExternalDeps
import com.example.impl.contacts.domain.receiver.BirthdayReceiverProvider
import com.example.themePicker.api.ThemeScreenApi
import com.github.terrakok.cicerone.Router

interface ContactsExternalDependencies : FeatureExternalDeps {
    val router: Router
    val context: Context
    val themeApi: ThemeScreenApi
    val mapApi: MapScreenApi
    val birthdayReceiverProvider: BirthdayReceiverProvider
}