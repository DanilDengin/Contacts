package com.example.impl.contacts.di

import com.example.di.FeatureScope
import com.example.impl.contacts.data.repository.ContactsRepositoryImpl
import com.example.impl.contacts.domain.repository.ContactsRepository
import com.example.impl.contacts.domain.time.CurrentTime
import com.example.impl.contacts.domain.time.CurrentTimeImpl
import com.example.impl.contacts.domain.useCases.ContactDetailsUseCase
import com.example.impl.contacts.domain.useCases.ContactDetailsUseCaseImpl
import com.example.impl.contacts.domain.useCases.ContactListUseCase
import com.example.impl.contacts.domain.useCases.ContactListUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface ContactsModule {

    @FeatureScope
    @Binds
    fun bindContactListUseCase(contactListUseCaseImpl: ContactListUseCaseImpl): ContactListUseCase

    @FeatureScope
    @Binds
    fun bindContactDetailsUseCase(contactDetailsUseCaseImpl: ContactDetailsUseCaseImpl): ContactDetailsUseCase

    @FeatureScope
    @Binds
    fun provideCurrentTime(currentTimeImpl: CurrentTimeImpl): CurrentTime

    @FeatureScope
    @Binds
    fun bindContactsRepository(contactsRepositoryImpl: ContactsRepositoryImpl): ContactsRepository
}