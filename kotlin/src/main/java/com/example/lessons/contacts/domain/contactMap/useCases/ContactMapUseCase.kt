package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.entity.ContactMap
import kotlinx.coroutines.flow.Flow

interface ContactMapUseCase {

    suspend fun getAddress(geocode: String): ApiResponse<Address?>

    suspend fun createContactMap(contactMap: ContactMap)

    suspend fun getAllContactMaps(): Flow<List<ContactMap>>

    suspend fun getContactMapById(id: String): ContactMap?

    suspend fun deleteContactMap(id: String)

}