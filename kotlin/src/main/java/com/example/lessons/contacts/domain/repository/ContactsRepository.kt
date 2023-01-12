package com.example.lessons.contacts.domain.repository

import com.example.lessons.contacts.domain.entity.Contact

interface ContactsRepository {

    var contact : Contact?
    val contacts: List<Contact>

    suspend fun getShortContactsDetails(): List<Contact>

    suspend fun getFullContactDetails(contactId: String): Contact?
}