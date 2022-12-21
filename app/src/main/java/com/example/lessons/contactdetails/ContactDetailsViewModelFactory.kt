package com.example.lessons.contactdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lessons.repositories.ContactsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ContactDetailsViewModelFactory @AssistedInject constructor(
    @Assisted private val id: String,
    private val contactsRepository: ContactsRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactDetailsViewModel(id, contactsRepository) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(contactId: String): ContactDetailsViewModelFactory
    }
}