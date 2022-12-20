package com.example.lessons.contactdetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lessons.repositories.ContactsRepository
import javax.inject.Inject

class ContactDetailsViewModelFactory @Inject constructor(
    private val id: String,
    private val context: Context,
    private val contactsRepository: ContactsRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactDetailsViewModel(id, context, contactsRepository) as T
    }
}