package com.example.lessons.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.db.database.ContactMapDatabase
import com.example.di.AppScope
import dagger.Module
import dagger.Provides

@Module
internal object DataModule {

    @AppScope
    @Provides
    fun provideContactMapDatabase(context: Context): ContactMapDatabase {
        return Room.databaseBuilder(
            context,
            ContactMapDatabase::class.java,
            CONTACT_MAP_DATABASE_NAME
        ).build()
    }

    @AppScope
    @Provides
    fun provideSharedPrefTheme(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREF_KEY, AppCompatActivity.MODE_PRIVATE)
    }

    private const val CONTACT_MAP_DATABASE_NAME = "contactsLocation"
    private const val SHARED_PREF_KEY = "appSharedPrefKey"
}