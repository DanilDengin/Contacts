package com.example.lessons.contactMap.di

import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit

interface MapComponentDependencies {

    fun getRetrofit(): Retrofit
}