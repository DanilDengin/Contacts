package com.example.contact.impl.presentation

import androidx.lifecycle.ViewModel
import com.example.contact.impl.di.ContactsExternalDependencies
import com.example.contact.impl.di.DaggerContactsComponent
import com.example.contact.impl.presentation.ContactsComponentDependenciesProvider.contactsExternalDependencies
import com.example.utils.delegate.unsafeLazy

internal class ContactComponentViewModel : ViewModel() {
    val contactsComponent by unsafeLazy {
        DaggerContactsComponent.factory()
            .create(checkNotNull(contactsExternalDependencies))
    }

    override fun onCleared() {
        super.onCleared()
        contactsExternalDependencies = null
    }
}

internal object ContactsComponentDependenciesProvider {
    var contactsExternalDependencies: ContactsExternalDependencies? = null
}
