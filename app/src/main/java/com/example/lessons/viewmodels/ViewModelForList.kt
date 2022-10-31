package com.example.lessons.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsListRepository

class ViewModelForList : ViewModel() {
    private val contactProvider = ContactsListRepository()
    private var users = MutableLiveData<List<Contact>>()
    private val handler = Handler(Looper.getMainLooper())

    init {
        Log.e("A", "VM created")
    }

    fun getUsers(context: Context): LiveData<List<Contact>> {
        loadUsers(context)
        return users
    }

    private fun loadUsers(context: Context) {
        Thread {
            handler.post {
                users.value = contactProvider.getShortContactsDetails(context)
            }
        }.start()
    }

    override fun onCleared() {
        Log.e("A", "Vm cleared")
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}