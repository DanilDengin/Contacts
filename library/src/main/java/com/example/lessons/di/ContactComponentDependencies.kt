package com.example.lessons.di

import com.example.lessons.contacts.domain.repository.local.ContactsRepository

interface ContactComponentDependencies {

    fun getContactsRepository(): ContactsRepository
}