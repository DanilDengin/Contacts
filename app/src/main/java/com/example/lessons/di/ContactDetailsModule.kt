package com.example.lessons.di

import android.content.Context
import com.example.lessons.contactdetails.ContactDetailsViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ContactDetailsModule(private val contactId: String) {

    @ContactDetailsScope
    @Provides
    fun provideViewModelFactory(context: Context) =
        ContactDetailsViewModelFactory(contactId, context)
}