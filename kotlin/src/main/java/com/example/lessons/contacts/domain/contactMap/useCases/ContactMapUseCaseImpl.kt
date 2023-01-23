package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.contactMap.QueryState
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import javax.inject.Inject

class ContactMapUseCaseImpl @Inject constructor(
    private val addressRepository: AddressRepository
) : ContactMapUseCase {

    override suspend fun getQueryState(geocode : String): QueryState {
        return addressRepository.queryState(geocode)
    }

    override suspend fun getData(geocode : String): Address? {
        return addressRepository.getAddress(geocode)
    }
}
