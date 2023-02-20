package com.example.impl.map.di

import com.example.db.database.ContactMapDatabase
import com.example.di.dependency.FeatureExternalDeps
import com.github.terrakok.cicerone.Router
import retrofit2.Retrofit

interface MapExternalDependencies: FeatureExternalDeps {
    val contactMapDatabase: ContactMapDatabase
    val retrofit: Retrofit
    val router: Router
}