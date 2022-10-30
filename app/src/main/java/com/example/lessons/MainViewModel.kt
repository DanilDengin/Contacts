package com.example.lessons

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val contactProvider = ContactProvider()
    private var users = MutableLiveData<ArrayList<Contact>>()
    private var user = MutableLiveData<Contact>()
    private val handler = Handler(Looper.getMainLooper())


    init {
        Log.e("A", "VM created")
    }

    fun getUsers(context: Context): LiveData<ArrayList<Contact>> {
        loadUsers(context)
        return users
    }

    private fun loadUsers(context: Context) {
        Thread {
            Thread.sleep(3000)
            handler.post {
                users.value = contactProvider.getShortContactsDetails(context)
            }
        }.start()
    }

    fun getUserDetail(id: String, context: Context): LiveData<Contact> {
        loadUserDetail(id, context)
        return user
    }

    private fun loadUserDetail( id: String, context: Context) {
        Thread {
            Thread.sleep(3000)
            handler.post {
                user.value = contactProvider.getFullContactDetails(id, context)
            }
        }.start()
    }

    override fun onCleared() {
        Log.e("A", "Vm cleared")
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}