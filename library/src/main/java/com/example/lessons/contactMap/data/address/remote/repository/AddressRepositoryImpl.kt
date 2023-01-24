package com.example.lessons.contactMap.data.address.remote.repository

import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.contactMap.data.address.remote.mappint.toAddress
import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddressRepositoryImpl @Inject constructor(
    private val addressService: AddressService
) : AddressRepository {

    override suspend fun getAddress(geocode: String): ApiResponse<Address?> {
        val response = withContext(Dispatchers.IO) {
            addressService.getAddress(geocode)
        }
        return when (response) {
            is ApiResponse.Success -> ApiResponse.Success(response.data.toAddress())
            is ApiResponse.Failure.HttpFailure -> response
            is ApiResponse.Failure.NetworkFailure -> response
            is ApiResponse.Failure.UnknownFailure -> response
        }
    }
}
