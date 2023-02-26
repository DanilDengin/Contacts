package com.example.common.address.domain.local.repository

import com.example.common.address.domain.entity.ContactMap
import kotlinx.coroutines.flow.Flow

interface ContactMapRepository {

    suspend fun createContactMap(contactMap: ContactMap)

    fun getAllContactMaps(): Flow<List<ContactMap>>

    fun getContactMapById(id: String): Flow<ContactMap?>

    suspend fun deleteContactMap(id: String)

}