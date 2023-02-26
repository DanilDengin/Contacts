package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.entity.ContactDetails
import kotlinx.coroutines.flow.Flow

internal interface ContactDetailsUseCase {

    suspend fun getContactDetailsById(id: String): Flow<ContactDetails?>

    suspend fun getAlarmDate(): Long
}