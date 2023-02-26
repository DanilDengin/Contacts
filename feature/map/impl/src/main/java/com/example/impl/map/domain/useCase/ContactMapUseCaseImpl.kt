package com.example.impl.map.domain.useCase

import com.example.common.address.domain.entity.ContactMap
import com.example.common.address.domain.local.repository.ContactMapRepository
import com.example.impl.map.domain.entity.ContactAddress
import com.example.impl.map.domain.repository.remote.AddressRepository
import com.example.network.response.ApiResponse
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ContactMapUseCaseImpl @Inject constructor(
    private val addressRepository: AddressRepository,
    private val contactMapRepository: ContactMapRepository
) : ContactMapUseCase {

    override suspend fun getAddress(
        latitude: String,
        longitude: String
    ): ApiResponse<ContactAddress?> {
        return addressRepository.getAddress(geocode = "$latitude,$longitude")
    }

    override suspend fun createContactMap(contactMap: ContactMap) {
        contactMapRepository.createContactMap(contactMap)
    }

    override fun getAllContactMaps(): Flow<List<ContactMap>> {
        return contactMapRepository.getAllContactMaps()
    }

    override fun getContactMapById(id: String): Flow<ContactMap?> {
        return contactMapRepository.getContactMapById(id)
    }

    override suspend fun deleteContactMap(id: String) {
        contactMapRepository.deleteContactMap(id)
    }
}
