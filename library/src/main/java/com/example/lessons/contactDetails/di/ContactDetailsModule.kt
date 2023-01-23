package com.example.lessons.contactDetails.di

import com.example.lessons.contacts.domain.contactDetails.useCases.ContactDetailsUseCase
import com.example.lessons.contacts.domain.contactDetails.useCases.ContactDetailsUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ContactDetailsModule {

    @ContactDetailsScope
    @Binds
    fun bindContactDetailsUseCase(contactDetailsUseCaseImpl: ContactDetailsUseCaseImpl): ContactDetailsUseCase
}