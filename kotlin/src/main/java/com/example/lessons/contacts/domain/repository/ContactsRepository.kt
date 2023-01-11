package com.example.lessons.contacts.domain.repository

import com.example.lessons.contacts.domain.entity.Contact

interface ContactsRepository {

    val contacts: ArrayList<Contact>

    suspend fun getShortContactsDetails(): List<Contact>

    suspend fun getFullContactDetails(contactId: String): Contact?
}