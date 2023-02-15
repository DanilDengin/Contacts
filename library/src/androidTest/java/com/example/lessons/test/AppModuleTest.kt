package com.example.lessons.test

import com.example.lessons.contactList.di.ContactListScope
import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCase
import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCaseImpl
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
interface AppModuleTest {
    companion object{
        @Singleton
        @Provides
        fun provideContactsRepository(): ContactsRepository = mockk()
    }

    @ContactListScope
    @Binds
    abstract fun bindContactListUseCase(contactListUseCaseImpl: ContactListUseCaseImpl): ContactListUseCase

}