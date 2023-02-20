package com.example.contact.impl.presentation

import androidx.lifecycle.ViewModel
import com.example.contact.impl.di.ContactsExternalDependencies
import com.example.contact.impl.di.DaggerContactsComponent
import com.example.contact.impl.presentation.ContactsComponentDependenciesProvider.featureDependencies
import com.example.utils.delegate.unsafeLazy

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