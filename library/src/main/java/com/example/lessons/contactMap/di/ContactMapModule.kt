package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import com.example.lessons.contactMap.data.address.local.room.model.ContactMapDao
import com.example.lessons.contactMap.data.address.local.room.repository.ContactMapRepositoryImpl
import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.contactMap.data.address.remote.repository.AddressRepositoryImpl
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCaseImpl
import com.example.lessons.contacts.domain.repository.local.ContactMapRepository
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface ContactMapModule {

    @ContactMapScope
    @Binds
    fun bindAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository

    @ContactMapScope
    @Binds
    fun bindContactMapRepository(contactMapRepositoryImpl: ContactMapRepositoryImpl): ContactMapRepository

    @ContactMapScope
    @Binds
    fun bindContactMapUseCase(contactMapUseCaseImpl: ContactMapUseCaseImpl): ContactMapUseCase

    companion object {
        @ContactMapScope
        @Provides
        fun provideRetrofit(retrofit: Retrofit): AddressService {
            return retrofit.create(AddressService::class.java)
        }

        @ContactMapScope
        @Provides
        fun provideContactMapDao(contactMapDatabase: ContactMapDatabase): ContactMapDao {
            return contactMapDatabase.getContactMapDao()
        }
    }

}