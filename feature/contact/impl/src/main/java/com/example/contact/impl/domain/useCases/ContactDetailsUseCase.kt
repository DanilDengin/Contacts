package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.entity.ContactDetails

internal interface ContactDetailsUseCase {

    suspend fun getContactDetailsById(id: String): ContactDetails?

    suspend fun getAlarmDate(): Long
}
