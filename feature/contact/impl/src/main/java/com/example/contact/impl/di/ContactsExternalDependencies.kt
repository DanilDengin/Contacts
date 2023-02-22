package com.example.contact.impl.di

import android.content.Context
import com.example.api.map.screen.MapScreenApi
import com.example.contact.impl.domain.receiver.BirthdayReceiverProvider
import com.example.di.dependency.FeatureExternalDeps
import com.example.themePicker.api.ThemeScreenApi
import com.github.terrakok.cicerone.Router

interface ContactsExternalDependencies : FeatureExternalDeps {
    val router: Router
    val context: Context
    val themeApi: ThemeScreenApi
    val mapApi: MapScreenApi
    val birthdayReceiverProvider: BirthdayReceiverProvider
}