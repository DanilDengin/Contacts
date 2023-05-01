package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.entity.ContactList

internal interface ContactListUseCase {

    suspend fun getContactList(): List<ContactList>

    suspend fun searchContactByQuery(query: String): List<ContactList>?
}
