package com.example.common.address.domain.local.repository

import com.example.common.address.domain.entity.ContactMap
import kotlinx.coroutines.flow.Flow

interface ContactMapRepository {

    suspend fun createContactMap(contactMap: ContactMap)

    fun getAllContactMaps(): Flow<List<ContactMap>>

    suspend fun getContactMapById(id: String): ContactMap?

    suspend fun deleteContactMap(id: String)

}