package com.example.lessons.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import dagger.Module
import dagger.Provides

@Module
object DataModule {

    @DataScope
    @Provides
    fun provideContactMapDatabase(context: Context): ContactMapDatabase {
        return Room.databaseBuilder(
            context,
            ContactMapDatabase::class.java,
            CONTACT_MAP_DATABASE_NAME
        ).build()
    }

    @DataScope
    @Provides
    fun provideSharedPrefTheme(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_KEY, AppCompatActivity.MODE_PRIVATE)
    }

    private const val CONTACT_MAP_DATABASE_NAME = "contactsLocation"
    private const val SHARED_PREF_KEY = "appSharedPrefKey"
}