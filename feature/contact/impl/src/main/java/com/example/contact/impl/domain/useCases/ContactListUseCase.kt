package com.example.contact.impl.domain.useCases

import com.example.contact.api.entity.Contact

internal interface ContactListUseCase {

    suspend fun getContactList(): List<Contact>

    suspend fun searchContactByQuery(query: String): List<Contact>?
}