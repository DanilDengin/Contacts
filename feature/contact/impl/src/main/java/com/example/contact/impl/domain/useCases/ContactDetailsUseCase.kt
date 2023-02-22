package com.example.contact.impl.domain.useCases

import com.example.contact.api.entity.Contact

internal interface ContactDetailsUseCase {

    suspend fun getContactById(id: String): Contact?

    suspend fun getAlarmDate(): Long
}