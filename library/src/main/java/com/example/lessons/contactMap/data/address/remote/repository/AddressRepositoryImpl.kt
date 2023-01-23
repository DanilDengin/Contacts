package com.example.lessons.contactMap.data.address.remote.repository

import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.contactMap.data.address.remote.mappint.toAddress
import com.example.lessons.contactMap.data.address.remote.model.AddressDto
import com.example.lessons.contacts.domain.contactMap.QueryState
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.repository.remote.AddressRepository
import com.example.lessons.utils.response.ApiResponse
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AddressRepositoryImpl (private val addressService: AddressService) :
    AddressRepository {

    private var address : Address? = null

    override suspend fun queryState(geocode: String): QueryState {
        val response = withContext(Dispatchers.IO) {
            addressService.getAddress(geocode)
        }
        return when (response) {
            is ApiResponse.Success -> {
                address = response.data.toAddress()
                return QueryState.SUCCESS
            }
            is ApiResponse.Failure.NetworkFailure -> QueryState.NETWORK_ERROR
            is ApiResponse.Failure.HttpFailure -> QueryState.SERVICE_ERROR
            is ApiResponse.Failure.UnknownFailure -> QueryState.UNKNOWN_ERROR
        }
    }

    override suspend fun getAddress(): Address? {
        return address
    }
}
