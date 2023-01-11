package com.example.lessons.contacts.domain.contactDetails.useCases

import com.example.lessons.contacts.domain.entity.Contact

interface ContactDetailsUseCase {

    suspend fun getContactById(id: String): Contact?

    fun getAlarmDate(contact: Contact): Long
}