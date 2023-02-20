package com.example.impl.contacts.di

import com.example.api.contacts.ContactsScreenApi
import com.example.impl.contacts.ContactsScreenApiImpl
import dagger.Binds
import dagger.Module

@Module
interface ContactsScreenApiModule {
    @Binds
    fun bindContactsScreenApi(contactsScreenApiImpl: ContactsScreenApiImpl): ContactsScreenApi
}