package com.example.lessons.di

import com.example.di.AppScope
import com.example.network.response.ApiResponseFactoryProvider
import com.example.network.retrofit.OkHttpClientProvider
import com.example.network.retrofit.RetrofitProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Retrofit

@Module
internal object NetworkModule {

    @AppScope
    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClientProvider.get()

    @AppScope
    @Provides
    fun provideApiResponseFactory(): CallAdapter.Factory = ApiResponseFactoryProvider.get()

    @AppScope
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        apiResponseFactory: CallAdapter.Factory
    ): Retrofit = RetrofitProvider.get(okHttpClient, YANDEX_URI, apiResponseFactory)

    private const val YANDEX_URI = "https://geocode-maps.yandex.ru/1.x/"
}
