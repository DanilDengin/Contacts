package com.example.lessons.contactdetails

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsRepository

class ContactDetailsViewModel(id: String, context: Context) : ViewModel() {

    private val contactsRepository = ContactsRepository()
    private val user = MutableLiveData<Contact>()

    init {
        loadUserDetail(id, context)
    }

    fun getUserDetails(): LiveData<Contact> {
        return user
    }

    private fun loadUserDetail(id: String, context: Context) {
        Thread {
            user.postValue(contactsRepository.getFullContactDetails(id, context))
        }.start()
    }
}