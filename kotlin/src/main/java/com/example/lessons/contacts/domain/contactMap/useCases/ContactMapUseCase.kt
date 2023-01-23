package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.entity.Address

interface ContactMapUseCase {

    suspend fun getAddress(geocode: String): Result<Address?>

}