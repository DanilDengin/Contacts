package com.example.lessons.contactDetails.di

import com.example.lessons.contacts.domain.contactDetails.useCases.ContactDetailsUseCase
import com.example.lessons.contacts.domain.contactDetails.useCases.ContactDetailsUseCaseImpl
import com.example.lessons.contacts.domain.utils.time.CurrentTime
import com.example.lessons.contacts.domain.utils.time.CurrentTimeImpl
import dagger.Binds
import dagger.Module

@Module
internal interface ContactDetailsModule {

    @ContactDetailsScope
    @Binds
    fun bindContactDetailsUseCase(contactDetailsUseCaseImpl: ContactDetailsUseCaseImpl): ContactDetailsUseCase

    @ContactDetailsScope
    @Binds
    fun provideCurrentTime(currentTimeImpl: CurrentTimeImpl): CurrentTime
}