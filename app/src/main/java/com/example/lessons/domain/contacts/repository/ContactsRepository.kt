package com.example.lessons.domain.contacts.repository

import com.example.lessons.domain.contacts.entity.Contact

interface ContactsRepository {

    fun getShortContactsDetails(): List<Contact>

    fun getFullContactDetails(id: String): Contact?
}