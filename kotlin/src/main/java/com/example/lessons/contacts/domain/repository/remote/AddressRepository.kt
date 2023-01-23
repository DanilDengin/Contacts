package com.example.lessons.contacts.domain.repository.remote

import com.example.lessons.contacts.domain.contactMap.QueryState
import com.example.lessons.contacts.domain.entity.Address

interface AddressRepository {

    suspend fun queryState(geocode: String): QueryState

    suspend fun getAddress(geocode: String): Address?

}