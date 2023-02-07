package com.example.lessons.di.contactListDetails

import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.di.provider.AppDependencies

interface ContactComponentDependencies : AppDependencies {

    fun getContactsRepository(): ContactsRepository
}