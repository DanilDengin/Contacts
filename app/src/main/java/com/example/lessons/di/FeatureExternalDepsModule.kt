package com.example.lessons.di

import com.example.contact.impl.di.ContactsExternalDependencies
import com.example.di.dependency.FeatureExternalDeps
import com.example.di.dependency.FeatureExternalDepsKey
import com.example.impl.map.di.MapExternalDependencies
import com.example.themePicker.impl.di.ThemeExternalDependencies
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal interface FeatureExternalDepsModule {

    @Binds
    @IntoMap
    @FeatureExternalDepsKey(ContactsExternalDependencies::class)
    fun bindContactsExternalDeps(appComponent: AppComponent): FeatureExternalDeps

    @Binds
    @IntoMap
    @FeatureExternalDepsKey(ThemeExternalDependencies::class)
    fun bindThemeExternalDeps(appComponent: AppComponent): FeatureExternalDeps

    @Binds
    @IntoMap
    @FeatureExternalDepsKey(MapExternalDependencies::class)
    fun bindMapExternalDeps(appComponent: AppComponent): FeatureExternalDeps
}
