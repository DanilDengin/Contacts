package com.example.impl.map.data.address.remote.api

import com.example.impl.map.data.address.remote.model.AddressDto
import com.example.network.response.ApiResponse
import com.example.utils.constans.HTTP_GEOCODING_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

internal interface AddressService {

    @GET("?apikey=$HTTP_GEOCODING_API_KEY&format=json")
    suspend fun getAddress(
        @Query("geocode") geocode: String
    ): ApiResponse<AddressDto>

}