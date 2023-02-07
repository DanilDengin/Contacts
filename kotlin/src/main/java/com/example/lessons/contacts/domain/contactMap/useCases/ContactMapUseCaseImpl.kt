package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.ContactAddress
import com.example.lessons.contacts.domain.entity.ContactMap
import com.example.lessons.contacts.domain.repository.local.ContactMapRepository
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ContactMapUseCaseImpl @Inject constructor(
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

    override suspend fun getContactMapById(id: String): ContactMap? {
        return contactMapRepository.getContactMapById(id)
    }

    override suspend fun deleteContactMap(id: String) {
        contactMapRepository.deleteContactMap(id)
    }
}
