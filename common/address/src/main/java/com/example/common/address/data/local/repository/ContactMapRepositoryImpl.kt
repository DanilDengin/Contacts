package com.example.common.address.data.local.repository

import com.example.common.address.domain.entity.ContactMap
import com.example.common.address.domain.entity.toContactMap
import com.example.common.address.domain.entity.toContactMapDbEntity
import com.example.common.address.domain.local.repository.ContactMapRepository
import com.example.db.model.ContactMapDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

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
