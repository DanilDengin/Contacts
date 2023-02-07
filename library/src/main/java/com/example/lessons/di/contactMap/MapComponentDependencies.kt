package com.example.lessons.di.contactMap

import com.example.lessons.contactMap.data.address.local.room.database.ContactMapDatabase
import com.example.lessons.di.provider.DataDependencies
import retrofit2.Retrofit

interface MapComponentDependencies : DataDependencies {

    fun getContactDatabase(): ContactMapDatabase

    fun getRetrofit(): Retrofit
}