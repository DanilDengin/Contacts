package com.example.lessons.di

import android.content.Context
import com.example.lessons.contactdetails.ContactDetailsViewModelFactory
import com.example.lessons.repositories.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule {
    @ContactDetailsScope
    @Provides
    fun provideViewModelFactory(
        contactId: String,
        context: Context,
        contactsRepository: ContactsRepository
    ) =
        ContactDetailsViewModelFactory(contactId, context, contactsRepository)
}

