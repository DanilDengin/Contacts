package com.example.lessons.contacts.domain.repository.remote

import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.entity.ContactAddress

interface AddressRepository {

    suspend fun getAddress(geocode: String): ApiResponse<ContactAddress?>

}