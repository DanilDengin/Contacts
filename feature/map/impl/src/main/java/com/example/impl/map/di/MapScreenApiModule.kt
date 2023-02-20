package com.example.impl.map.di

import com.example.api.map.MapScreenApi
import com.example.impl.map.MapScreenApiImpl
import dagger.Binds
import dagger.Module

@Module
interface MapScreenApiModule {
    @Binds
    fun bindMapScreenApi(mapScreenApiImpl: MapScreenApiImpl): MapScreenApi
}