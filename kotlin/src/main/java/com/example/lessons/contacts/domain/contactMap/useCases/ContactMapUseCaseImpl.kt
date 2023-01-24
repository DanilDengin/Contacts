package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import javax.inject.Inject

class ContactMapUseCaseImpl @Inject constructor(
    private val addressRepository: AddressRepository
) : ContactMapUseCase {

    override suspend fun getAddress(geocode: String): ApiResponse<Address?> {
        return addressRepository.getAddress(geocode)
    }
}
