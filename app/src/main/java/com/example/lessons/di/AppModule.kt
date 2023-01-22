package com.example.lessons.di

import android.content.Context
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.data.contacts.repository.local.ContactsRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object AppModule {

    @Singleton
    @Provides
    fun provideContactsRepository(context: Context): ContactsRepository {
        return ContactsRepositoryImpl(context = context)
    }
}