package com.example.lessons.contactMap.di

import com.example.lessons.contactMap.data.address.remote.api.AddressService
import com.example.lessons.utils.response.ApiResponseCallAdapterFactory
import com.example.library.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal object ContactMapModule {

    @ContactMapScope
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            })
            .build()
    }

    @ContactMapScope
    @Provides
    fun provideAddressService(okHttpClient: OkHttpClient): AddressService {
        return Retrofit.Builder()
            .baseUrl("https://geocode-maps.yandex.ru/1.x/")
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AddressService::class.java)
    }
}