package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.entity.ContactMap
import com.example.lessons.contacts.domain.repository.local.ContactMapRepository
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ContactMapUseCaseImpl @Inject constructor(
    private val addressRepository: AddressRepository,
    private val contactMapRepository: ContactMapRepository
) : ContactMapUseCase {

    private var contactMapsFlow: Flow<List<ContactMap>>? = null

    override suspend fun getAddress(geocode: String): ApiResponse<Address?> {
        return addressRepository.getAddress(geocode)
    }

    override suspend fun createContactMap(contactMap: ContactMap) {
        contactMapRepository.createContactMap(contactMap)
    }

    override suspend fun getAllContactMaps(): Flow<List<ContactMap>> {
        return saveContactMapsFlow()
    }

    override suspend fun getContactMapById(id: String): ContactMap? {
        return contactMapRepository.getContactMapById(id)
    }

    override suspend fun deleteContactMap(id: String) {
        contactMapRepository.deleteContactMap(id)
    }

    private suspend fun saveContactMapsFlow(): Flow<List<ContactMap>> {
        return if (contactMapsFlow != null) {
            requireNotNull(contactMapsFlow)
        } else {
            contactMapsFlow = contactMapRepository.getAllContactMaps()
            requireNotNull(contactMapsFlow)
        }
    }
}
