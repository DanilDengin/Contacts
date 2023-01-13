package com.example.lessons.contactDetails.di

import com.example.lessons.contacts.domain.contactDetails.useCases.ContactDetailsUseCase
import com.example.lessons.contacts.domain.contactDetails.useCases.ContactDetailsUseCaseImpl
import com.example.lessons.contacts.domain.repository.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
internal object ContactDetailsModule {

    @ContactDetailsScope
    @Provides
    fun provideContactDetailsUseCase(contactsRepository: ContactsRepository): ContactDetailsUseCase {
        return ContactDetailsUseCaseImpl(contactsRepository = contactsRepository)
    }
}