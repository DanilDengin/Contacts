package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.repository.remote.AddressRepository

class ContactMapUseCaseImpl(private val addressRepository: AddressRepository) : ContactMapUseCase {

    override suspend fun getAddress(geocode: String): Result<Address?> {
        return addressRepository.getAddress(geocode)
    }
}
