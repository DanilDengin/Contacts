package com.example.lessons.contactMap.data.address.local.room.repository

import com.example.lessons.contactMap.data.address.local.room.model.ContactMapDao
import com.example.lessons.contactMap.data.address.local.room.model.toContactMap
import com.example.lessons.contactMap.data.address.local.room.model.toContactMapDbEntity
import com.example.lessons.contacts.domain.entity.ContactMap
import com.example.lessons.contacts.domain.repository.local.ContactMapRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class ContactMapRepositoryImpl @Inject constructor(
    private val contactMapDao: ContactMapDao
) : ContactMapRepository {

    override suspend fun createContactMap(contactMap: ContactMap) {
        val entity = contactMap.toContactMapDbEntity()
        return contactMapDao.createContactAddress(entity)
    }

    override fun getAllContactMaps(): Flow<List<ContactMap>> {
        return contactMapDao.getAllAddresses()
            .map { list ->
                list
                    .map { contactMapDbEntity ->
                        contactMapDbEntity.toContactMap()
                    }
            }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getContactMapById(id: String): ContactMap? {
        return contactMapDao.getAddressById(id)?.toContactMap()
    }

    override suspend fun deleteContactMap(id: String) {
        contactMapDao.deleteContactAddress(id)
    }
}