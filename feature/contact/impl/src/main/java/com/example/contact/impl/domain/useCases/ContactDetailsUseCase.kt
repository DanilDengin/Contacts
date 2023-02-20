package com.example.contact.impl.domain.useCases

internal interface ContactDetailsUseCase {

    suspend fun getContactById(id: String): com.example.entity.Contact?

    suspend fun getAlarmDate(): Long
}