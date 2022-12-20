package com.example.lessons.di

import android.content.Context
import com.example.lessons.repositories.ContactsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesContactRepository(context: Context) = ContactsRepository(context)
}