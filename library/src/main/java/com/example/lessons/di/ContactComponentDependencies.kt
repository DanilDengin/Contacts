package com.example.lessons.di

import com.example.lessons.contacts.domain.repository.ContactsRepository
import javax.inject.Singleton

@Singleton
interface ContactComponentDependencies {

    fun getContactsRepository(): ContactsRepository
}