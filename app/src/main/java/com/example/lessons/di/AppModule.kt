package com.example.lessons.di

import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.data.contacts.repository.local.ContactsRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal interface AppModule {

    @Singleton
    @Binds
    fun bindContactsRepository(contactsRepositoryImpl: ContactsRepositoryImpl): ContactsRepository
}