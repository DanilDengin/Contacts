package com.example.impl.map.di

import com.example.db.database.ContactMapDatabase
import com.example.db.model.ContactMapDao
import com.example.di.FeatureScope
import com.example.impl.map.data.address.local.room.repository.ContactMapRepositoryImpl
import com.example.impl.map.data.address.remote.api.AddressService
import com.example.impl.map.data.address.remote.repository.AddressRepositoryImpl
import com.example.impl.map.domain.repository.local.ContactMapRepository
import com.example.impl.map.domain.repository.remote.AddressRepository
import com.example.impl.map.domain.useCases.ContactMapUseCase
import com.example.impl.map.domain.useCases.ContactMapUseCaseImpl
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
    fun bindContactMapRepository(contactMapRepositoryImpl: ContactMapRepositoryImpl): ContactMapRepository

    @FeatureScope
    @Binds
    fun bindContactMapUseCase(contactMapUseCaseImpl: ContactMapUseCaseImpl): ContactMapUseCase

    companion object {
        @FeatureScope
        @Provides
        fun provideRetrofit(retrofit: Retrofit): AddressService {
            return retrofit.create(AddressService::class.java)
        }

        @FeatureScope
        @Provides
        fun provideContactMapDao(contactMapDatabase: ContactMapDatabase): ContactMapDao {
            return contactMapDatabase.getContactMapDao()
        }
    }
}