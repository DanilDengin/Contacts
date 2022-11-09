package com.example.lessons.contactslist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsRepository

class ContactsListViewModel(context: Context) : ViewModel() {
    private val contactsRepository = ContactsRepository()
    private val users = MutableLiveData<List<Contact>?>()

    init {
        loadUsers(context)
    }

    fun getUsers(): LiveData<List<Contact>?> {
        return users
    }

    fun loadUsers(context: Context) {
        Thread {
            users.postValue(contactsRepository.getShortContactsDetails(context))
        }.start()
    }

    fun updateUsers(filteredList: List<Contact>?) {
        users.postValue(filteredList)
    }

}