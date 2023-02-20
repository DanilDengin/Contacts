package com.example.impl.contacts.domain.useCases

import com.example.entity.Contact

interface ContactListUseCase {

    suspend fun getContactList(): List<com.example.entity.Contact>

    suspend fun searchContactByQuery(query: String): List<com.example.entity.Contact>?
}