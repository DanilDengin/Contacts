package com.example.lessons.contactslist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsRepository
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newSingleThreadExecutor

class ContactListViewModel(context: Context) : ViewModel() {
    private val contactsRepository = ContactsRepository()
    private val users = MutableLiveData<List<Contact>?>()

    init {
        loadUsers(context)
    }

    fun getUsers(): LiveData<List<Contact>?> {
        return users
    }

    private fun loadUsers(context: Context) {
        Thread {
            users.postValue(contactsRepository.getShortContactsDetails(context))
        }.start()
    }

    fun filterUsers(query: String?, context: Context) {
        if (query == null || query.isBlank()) {
            loadUsers(context = context)
        } else {
            val executor : ExecutorService = newSingleThreadExecutor()
            executor.execute{
                val trimmedQuery = query.trim()
                val filteredContacts = ArrayList<Contact>()
                users.value?.forEach { contact ->
                    if (contact.name.contains(trimmedQuery, ignoreCase = true))
                        filteredContacts.add(contact)
                }
                users.postValue(filteredContacts)
                executor.shutdown()
            }
        }
    }
}