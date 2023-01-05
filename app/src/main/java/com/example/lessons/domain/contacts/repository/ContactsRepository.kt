package com.example.lessons.domain.contacts.repository

import com.example.lessons.domain.contacts.entity.Contact

interface ContactsRepository {

    suspend fun getShortContactsDetails(): List<Contact>

    suspend fun getFullContactDetails(id: String): Contact?
}