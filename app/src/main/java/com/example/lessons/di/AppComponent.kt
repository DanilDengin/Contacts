package com.example.lessons.di

import android.content.Context
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.di.contactListDetails.ContactComponentDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton
import retrofit2.Retrofit

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class])
internal interface AppComponent : ContactComponentDependencies {

    override fun getContactsRepository(): ContactsRepository

    fun getContext(): Context

    fun getRetrofit(): Retrofit

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}