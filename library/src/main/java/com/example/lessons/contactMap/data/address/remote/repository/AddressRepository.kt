package com.example.lessons.contactMap.data.address.remote.repository

import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.contactMap.data.address.remote.model.AddressItem
import com.example.lessons.utils.response.ApiResponse
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class AddressRepository @Inject constructor(private val addressService: AddressService) {

    suspend fun getAddress(geocode: String): ApiResponse<AddressItem> {
        return withContext(Dispatchers.IO) {
            addressService.getAddress(geocode)
        }
    }
}