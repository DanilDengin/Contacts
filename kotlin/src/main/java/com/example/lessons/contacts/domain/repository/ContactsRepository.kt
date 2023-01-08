package com.example.lessons.contacts.domain.repository

import com.example.lessons.contacts.domain.entity.Contact

interface ContactsRepository {

    suspend fun getShortContactsDetails(): List<Contact>

    suspend fun getFullContactDetails(id: String): Contact?
}