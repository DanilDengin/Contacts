package com.example.lessons.di

import android.content.Context
import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import com.example.lessons.di.contactMap.MapComponentDependencies
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.di.contactListDetails.ContactComponentDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import retrofit2.Retrofit

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DataModule::class])
internal interface AppComponent : ContactComponentDependencies, MapComponentDependencies {

    override fun getContactsRepository(): ContactsRepository

    override fun getRetrofit(): Retrofit

    override fun getContactDatabase(): ContactMapDatabase

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}