package com.example.lessons.di

import com.example.contact.impl.domain.receiver.BirthdayReceiverProvider
import com.example.di.AppScope
import com.example.lessons.receiver.BirthdayReceiverProviderImpl
import dagger.Binds
import dagger.Module

@Module
interface ReceiverBindingModule {

    @AppScope
    @Binds
    fun bindBirthdayReceiver(birthdayReceiverProviderImpl: BirthdayReceiverProviderImpl): BirthdayReceiverProvider
}