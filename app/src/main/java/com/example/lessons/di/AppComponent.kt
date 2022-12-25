package com.example.lessons.di

import android.content.Context
import com.example.lessons.data.contacts.repository.ContactsRepositoryImpl
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component
interface AppComponent {
    fun getAppContext(): Context
    fun getContactRepository(): ContactsRepositoryImpl

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}