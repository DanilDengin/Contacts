package com.example.impl.map.domain.useCases

import com.example.impl.map.domain.entity.ContactAddress
import com.example.impl.map.domain.entity.ContactMap
import com.example.network.response.ApiResponse
import kotlinx.coroutines.flow.Flow

internal interface ContactMapUseCase {

    suspend fun getAddress(latitude: String, longitude: String): ApiResponse<ContactAddress?>

    suspend fun createContactMap(contactMap: ContactMap)

    fun getAllContactMaps(): Flow<List<ContactMap>>

    suspend fun getContactMapById(id: String): ContactMap?

    suspend fun deleteContactMap(id: String)

}