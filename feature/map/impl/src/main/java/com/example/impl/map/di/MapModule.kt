package com.example.impl.map.di

import com.example.di.FeatureScope
import com.example.impl.map.data.address.remote.api.AddressService
import com.example.impl.map.data.address.remote.repository.AddressRepositoryImpl
import com.example.impl.map.domain.repository.remote.AddressRepository
import com.example.impl.map.domain.useCase.ContactMapUseCase
import com.example.impl.map.domain.useCase.ContactMapUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
internal interface MapModule {

    @FeatureScope
    @Binds
    fun bindAddressRepository(addressRepositoryImpl: AddressRepositoryImpl): AddressRepository

    @FeatureScope
    @Binds
    fun bindContactMapUseCase(contactMapUseCaseImpl: ContactMapUseCaseImpl): ContactMapUseCase

    companion object {
        @FeatureScope
        @Provides
        fun provideRetrofit(retrofit: Retrofit): AddressService {
            return retrofit.create(AddressService::class.java)
        }
    }
}
