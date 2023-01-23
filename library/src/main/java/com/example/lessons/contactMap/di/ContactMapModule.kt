package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.contactMap.data.address.remote.repository.AddressRepositoryImpl
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal object ContactMapModule {

    @ContactMapScope
    @Provides
    fun provideRetrofit(retrofit: Retrofit): AddressService {
        return retrofit.create(AddressService::class.java)
    }

    @ContactMapScope
    @Provides
    fun provideContactMapUseCase(addressRepository: AddressRepository): ContactMapUseCase {
        return ContactMapUseCaseImpl(addressRepository = addressRepository)
    }

    @ContactMapScope
    @Provides
    fun provideAddressRepository(addressService: AddressService): AddressRepository {
        return AddressRepositoryImpl(addressService)
    }
}