package com.example.impl.map.domain.repository.remote

import com.example.impl.map.domain.entity.ContactAddress
import com.example.network.response.ApiResponse

internal interface AddressRepository {

    suspend fun getAddress(geocode: String): ApiResponse<ContactAddress?>
}