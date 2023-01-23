package com.example.lessons.contactMap.data.address.remote.api

import com.example.lessons.contactMap.data.address.remote.model.AddressDto
import com.example.lessons.utils.constans.HTTP_GEOCODING_API_KEY
import com.example.lessons.utils.response.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

internal interface AddressService {

    @GET("?apikey=$HTTP_GEOCODING_API_KEY&format=json")
    suspend fun getAddress(
        @Query("geocode") geocode: String
    ): ApiResponse<AddressDto>

}