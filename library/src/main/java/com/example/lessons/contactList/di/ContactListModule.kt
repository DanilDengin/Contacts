package com.example.lessons.contactList.di

import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCase
import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ContactListModule {

    @ContactListScope
    @Binds
    fun bindContactListUseCase(contactListUseCaseImpl: ContactListUseCaseImpl): ContactListUseCase

}