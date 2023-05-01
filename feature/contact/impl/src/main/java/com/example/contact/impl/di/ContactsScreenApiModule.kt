package com.example.contact.impl.di

import com.example.contact.api.screen.ContactsScreenApi
import com.example.contact.impl.ContactsScreenApiImpl
import dagger.Binds
import dagger.Module

@Module
interface ContactsScreenApiModule {
    @Binds
    fun bindContactsScreenApi(contactsScreenApiImpl: ContactsScreenApiImpl): ContactsScreenApi
}
