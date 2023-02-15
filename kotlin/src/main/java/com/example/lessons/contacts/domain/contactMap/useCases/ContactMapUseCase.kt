package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.utils.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.ContactAddress
import com.example.lessons.contacts.domain.entity.ContactMap
import kotlinx.coroutines.flow.Flow

interface ContactMapUseCase {

    suspend fun getAddress(latitude: String, longitude: String): ApiResponse<ContactAddress?>

    suspend fun createContactMap(contactMap: ContactMap)

    fun getAllContactMaps(): Flow<List<ContactMap>>

    suspend fun getContactMapById(id: String): ContactMap?

    suspend fun deleteContactMap(id: String)

}