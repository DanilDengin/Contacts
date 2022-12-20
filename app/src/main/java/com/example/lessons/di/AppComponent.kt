package com.example.lessons.di

import android.content.Context
import com.example.lessons.repositories.ContactsRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(contactsRepository: ContactsRepository)

    fun getAppContext(): Context
    fun getContactRepository(): ContactsRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}