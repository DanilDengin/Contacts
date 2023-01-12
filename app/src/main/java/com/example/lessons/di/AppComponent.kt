package com.example.lessons.di

import android.content.Context
import com.example.lessons.contacts.domain.repository.ContactsRepository
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
internal interface AppComponent : ContactComponentDependencies {

    override fun getContactsRepository(): ContactsRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}