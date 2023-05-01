package com.example.impl.map.data.address.remote.repository

import com.example.impl.map.data.address.remote.api.AddressService
import com.example.impl.map.data.address.remote.model.toAddress
import com.example.impl.map.domain.entity.ContactAddress
import com.example.impl.map.domain.repository.remote.AddressRepository
import com.example.network.response.ApiResponse
import javax.inject.Inject

internal class AddressRepositoryImpl @Inject constructor(
    private val addressService: AddressService
) : AddressRepository {

    override suspend fun getAddress(geocode: String): ApiResponse<ContactAddress?> {
        return when (val response = addressService.getAddress(geocode)) {
            is ApiResponse.Success -> ApiResponse.Success(response.data.toAddress())
            is ApiResponse.Failure.HttpFailure -> response
            is ApiResponse.Failure.NetworkFailure -> response
            is ApiResponse.Failure.UnknownFailure -> response
        }
    }
}
