package com.example.lessons.contactList.di

import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCase
import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCaseImpl
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import dagger.Module
import dagger.Provides

@Module
internal object ContactListModule {

    @ContactListScope
    @Provides
    fun provideContactListUseCase(contactsRepository: ContactsRepository): ContactListUseCase {
        return ContactListUseCaseImpl(contactsRepository = contactsRepository)
    }

}