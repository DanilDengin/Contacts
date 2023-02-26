package com.example.contact.impl.domain.repository

import com.example.contact.impl.data.model.ContactPhoneDb

internal interface ContactsRepository {

    suspend fun getShortContactsDetails(): List<ContactPhoneDb>

    suspend fun getFullContactDetails(contactId: String): ContactPhoneDb?
}