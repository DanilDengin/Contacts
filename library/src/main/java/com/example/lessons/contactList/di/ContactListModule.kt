package com.example.lessons.contactList.di

import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCase
import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCaseImpl
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
internal interface ContactListModule {

    @ContactListScope
    @Binds
    fun bindContactListUseCase(contactListUseCaseImpl: ContactListUseCaseImpl): ContactListUseCase

}