package com.example.impl.contacts.domain.repository

import com.example.entity.Contact

interface ContactsRepository {

    suspend fun getShortContactsDetails(): List<com.example.entity.Contact>

    suspend fun getFullContactDetails(contactId: String): com.example.entity.Contact?
}