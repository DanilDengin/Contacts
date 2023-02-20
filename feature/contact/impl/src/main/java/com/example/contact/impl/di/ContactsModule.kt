package com.example.contact.impl.di

import com.example.contact.impl.data.repository.ContactsRepositoryImpl
import com.example.contact.impl.domain.repository.ContactsRepository
import com.example.contact.impl.domain.time.CurrentTime
import com.example.contact.impl.domain.time.CurrentTimeImpl
import com.example.contact.impl.domain.useCases.ContactDetailsUseCase
import com.example.contact.impl.domain.useCases.ContactDetailsUseCaseImpl
import com.example.contact.impl.domain.useCases.ContactListUseCase
import com.example.contact.impl.domain.useCases.ContactListUseCaseImpl
import com.example.di.FeatureScope
import dagger.Binds
import dagger.Module

@Module
internal interface ContactsModule {

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