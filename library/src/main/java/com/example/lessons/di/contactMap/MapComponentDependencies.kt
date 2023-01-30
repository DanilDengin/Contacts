package com.example.lessons.di.contactMap

import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import com.example.lessons.di.provider.DiDependencies
import retrofit2.Retrofit

interface MapComponentDependencies : DiDependencies {

    fun getRetrofit(): Retrofit

    fun getContactDatabase(): ContactMapDatabase

}