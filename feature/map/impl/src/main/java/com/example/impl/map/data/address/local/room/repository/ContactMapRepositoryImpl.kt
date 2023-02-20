package com.example.impl.map.data.address.local.room.repository

import com.example.db.model.ContactMapDao
import com.example.impl.map.data.address.local.room.model.toContactMap
import com.example.impl.map.data.address.local.room.model.toContactMapDbEntity
import com.example.impl.map.domain.entity.ContactMap
import com.example.impl.map.domain.repository.local.ContactMapRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ContactMapRepositoryImpl @Inject constructor(
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