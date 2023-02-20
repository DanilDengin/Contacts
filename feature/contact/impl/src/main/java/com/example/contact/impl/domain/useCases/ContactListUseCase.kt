package com.example.contact.impl.domain.useCases

import com.example.entity.Contact

interface ContactListUseCase {

    suspend fun getContactList(): List<Contact>

    suspend fun searchContactByQuery(query: String): List<Contact>?
}