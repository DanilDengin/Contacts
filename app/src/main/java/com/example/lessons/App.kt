package com.example.lessons

import android.app.Application
import com.example.lessons.contactDetails.di.ContactDetailsComponentDependencies
import com.example.lessons.contactDetails.di.ContactDetailsComponentDependenciesProvider
import com.example.lessons.contactList.di.ContactListComponentDependencies
import com.example.lessons.contactList.di.ContactListComponentDependenciesProvider
import com.example.lessons.di.AppComponent
import com.example.lessons.di.DaggerAppComponent


class App : Application(), ContactListComponentDependenciesProvider,
    ContactDetailsComponentDependenciesProvider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory()
            .create(this)
    }

    override fun getContactListComponentDependencies(): ContactListComponentDependencies {
        return appComponent
    }

    override fun getContactDetailsComponentDependencies(): ContactDetailsComponentDependencies {
        return appComponent
    }
}