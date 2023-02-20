package com.example.impl.map.domain.repository.local

import com.example.impl.map.domain.entity.ContactMap
import kotlinx.coroutines.flow.Flow

interface ContactMapRepository {

    suspend fun createContactMap(contactMap: ContactMap)

    fun getAllContactMaps(): Flow<List<ContactMap>>

    suspend fun getContactMapById(id: String): ContactMap?

    suspend fun deleteContactMap(id: String)

}