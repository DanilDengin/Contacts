package com.example.impl.contacts.domain.useCases

interface ContactDetailsUseCase {

    suspend fun getContactById(id: String): com.example.entity.Contact?

    suspend fun getAlarmDate(): Long
}