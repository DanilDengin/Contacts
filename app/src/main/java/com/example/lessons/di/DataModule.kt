package com.example.lessons.di

import android.content.Context
import androidx.room.Room
import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Singleton
    @Provides
    fun provideContactMapDatabase(context: Context): ContactMapDatabase {
        return Room.databaseBuilder(
            context,
            ContactMapDatabase::class.java,
            CONTACT_MAP_DATABASE_NAME
        ).build()
    }

    private const val CONTACT_MAP_DATABASE_NAME = "contactsLocation"
}