package com.example.lessons.di

import android.content.Context
import com.example.lessons.contactlist.ContactListViewModelFactory
import com.example.lessons.repositories.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {

    @ContactListScope
    @Provides
    fun provideViewModel(context: Context, contactsRepository: ContactsRepository) =
        ContactListViewModelFactory(context, contactsRepository)
}