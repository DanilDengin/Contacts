package com.example.lessons.contactdetails

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ContactDetailsViewModelFactory(private val id: String, private val context: Context) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContactDetailsViewModel(id, context) as T
    }
}