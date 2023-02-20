package com.example.contact.impl.domain.repository

import com.example.entity.Contact

interface ContactsRepository {

    suspend fun getShortContactsDetails(): List<Contact>

    suspend fun getFullContactDetails(contactId: String): Contact?
}