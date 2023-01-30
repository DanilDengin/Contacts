package com.example.lessons.di.contactListDetails

import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.di.provider.DiDependencies

interface ContactComponentDependencies : DiDependencies {

    fun getContactsRepository(): ContactsRepository
}