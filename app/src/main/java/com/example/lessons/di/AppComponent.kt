package com.example.lessons.di

import android.content.Context
import com.example.lessons.App
import com.example.lessons.MainActivity
import com.example.lessons.contactlist.ContactListFragment
import com.example.lessons.contactlist.ContactListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(contactListViewModel: ContactListViewModel)
    fun provideAppContext() : Context
}