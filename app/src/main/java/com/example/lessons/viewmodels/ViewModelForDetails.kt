package com.example.lessons.viewmodels

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactDetailsRepository

class ViewModelForDetails(private val id: String) : ViewModel() {

    private val contactDetailsProvider = ContactDetailsRepository()
    private var user = MutableLiveData<Contact>()
    private val handler = Handler(Looper.getMainLooper())

    init {
        Log.e("A", "VM created")
    }

    fun getUserDetails(context: Context): LiveData<Contact> {
        loadUserDetail(id, context)
        return user
    }

    private fun loadUserDetail(id: String, context: Context) {
        Thread {
            handler.post {
                user.value = contactDetailsProvider.getFullContactDetails(id, context)
            }
        }.start()
    }

    override fun onCleared() {
        Log.e("A", "Vm cleared")
        handler.removeCallbacksAndMessages(null)
        super.onCleared()
    }
}