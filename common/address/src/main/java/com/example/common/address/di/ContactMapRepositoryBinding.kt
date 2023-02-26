package com.example.common.address.di

import com.example.common.address.data.local.repository.ContactMapRepositoryImpl
import com.example.common.address.domain.local.repository.ContactMapRepository
import com.example.db.database.ContactMapDatabase
import com.example.db.model.ContactMapDao
import com.example.di.FeatureScope
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface ContactMapRepositoryBinding {

    @FeatureScope
    @Binds
    fun bindContactMapRepository(contactMapRepositoryImpl: ContactMapRepositoryImpl): ContactMapRepository

    companion object {
        @FeatureScope
        @Provides
        fun provideContactMapDao(contactMapDatabase: ContactMapDatabase): ContactMapDao {
            return contactMapDatabase.getContactMapDao()
        }
    }
}