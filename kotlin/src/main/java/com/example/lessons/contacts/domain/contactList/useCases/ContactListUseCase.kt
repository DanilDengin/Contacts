package com.example.lessons.contacts.domain.contactList.useCases

import com.example.lessons.contacts.domain.entity.Contact

interface ContactListUseCase {

    suspend fun getContactList(): List<Contact>

    suspend fun searchContactByQuery(query: String): List<Contact>?
}