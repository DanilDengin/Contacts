package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.entity.ContactMap
import com.example.lessons.contacts.domain.repository.local.ContactMapRepository
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class ContactMapUseCaseImpl @Inject constructor(
    private val addressRepository: AddressRepository,
    private val contactMapRepository: ContactMapRepository
) : ContactMapUseCase {

    override suspend fun getAddress(geocode: String): ApiResponse<Address?> {
        return addressRepository.getAddress(geocode)
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
