package com.example.lessons.contacts.domain.repository.remote

import com.example.lessons.contacts.domain.entity.Address

interface AddressRepository {

    suspend fun getAddress(geocode: String): Result<Address?>

}