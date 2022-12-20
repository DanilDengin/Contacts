package com.example.lessons.contactlist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lessons.repositories.ContactsRepository
import javax.inject.Inject

class ContactListViewModelFactory @Inject constructor(
    private val context: Context,
    private val contactsRepository: ContactsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactListViewModel(context, contactsRepository) as T
    }
}