package com.example.lessons.contacts.domain.repository.local

import com.example.lessons.contacts.domain.entity.Contact

interface ContactsRepository {

    suspend fun getShortContactsDetails(): List<Contact>

    suspend fun getFullContactDetails(contactId: String): Contact?
}