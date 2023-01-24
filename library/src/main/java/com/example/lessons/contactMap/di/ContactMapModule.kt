package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.contactMap.data.address.remote.repository.AddressRepositoryImpl
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCaseImpl
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal abstract class ContactMapModule {

    @ContactMapScope
    @Binds
    abstract fun bindAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository

    @ContactMapScope
    @Binds
    abstract fun bindContactMapUseCase(contactMapUseCaseImpl: ContactMapUseCaseImpl): ContactMapUseCase

    companion object {
        @ContactMapScope
        @Provides
        fun provideRetrofit(retrofit: Retrofit): AddressService {
            return retrofit.create(AddressService::class.java)
        }
    }

}