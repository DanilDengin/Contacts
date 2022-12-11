package com.example.lessons.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

//    @Singleton
//    @Named("application_context")
//    @Provides
//    fun providesAppContext(): Context = context.applicationContext

    @Singleton
    @Provides
    fun providesApplicationContext(): Context = context
}