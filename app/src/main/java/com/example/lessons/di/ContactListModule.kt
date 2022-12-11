package com.example.lessons.di

import android.content.Context
import com.example.lessons.contactlist.ContactListViewModelFactory
import dagger.Module
import dagger.Provides

@Module
class ContactListModule {

    @ContactListScope
    @Provides
    fun provideViewModel(context : Context) = ContactListViewModelFactory(context)

}