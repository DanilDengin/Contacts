package com.example.lessons.di

import com.example.lessons.utils.response.ApiResponseCallAdapterFactory
import com.example.library.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
object NetworkModule {

    @Singleton
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

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAP_YANDEX_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private const val MAP_YANDEX_BASE_URL = "https://geocode-maps.yandex.ru/1.x/"
}