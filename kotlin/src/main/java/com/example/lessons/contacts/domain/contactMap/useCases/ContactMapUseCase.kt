package com.example.lessons.contacts.domain.contactMap.useCases

import com.example.lessons.contacts.domain.contactMap.QueryState
import com.example.lessons.contacts.domain.entity.Address

interface ContactMapUseCase {

    suspend fun getQueryState(geocode : String): QueryState

    suspend fun getData(): Address?

}