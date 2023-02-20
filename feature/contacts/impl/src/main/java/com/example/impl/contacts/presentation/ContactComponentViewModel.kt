package com.example.impl.contacts.presentation

import androidx.lifecycle.ViewModel
import com.example.impl.contacts.di.ContactsExternalDependencies
import com.example.impl.contacts.di.DaggerContactsComponent
import com.example.impl.contacts.presentation.ContactsComponentDependenciesProvider.featureDependencies
import com.example.lessons.utils.delegate.unsafeLazy

class ContactComponentViewModel : ViewModel() {
    val component by unsafeLazy {
        DaggerContactsComponent.factory()
            .create(checkNotNull(featureDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        featureDependencies = null
    }
}

object ContactsComponentDependenciesProvider {
    var featureDependencies: ContactsExternalDependencies? = null
}