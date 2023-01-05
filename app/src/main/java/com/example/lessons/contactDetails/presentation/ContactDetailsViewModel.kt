package com.example.lessons.contactDetails.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.data.contacts.repository.ContactsRepositoryImpl
import com.example.lessons.domain.contacts.entity.Contact
import com.example.lessons.utils.liveData.SingleLiveEvent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactDetailsViewModel @AssistedInject constructor(
    @Assisted id: String,
    private val contactsRepository: ContactsRepositoryImpl
) : ViewModel() {

    private companion object {
        val CONTACT_DETAILS_VIEW_MODEL_TAG: String = ContactDetailsViewModel::class.java.simpleName
    }

    val user: LiveData<Contact> get() = _user
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    val exceptionState: LiveData<Unit> get() = _exceptionState
    private val _user = MutableLiveData<Contact>()
    private val _progressBarState = MutableLiveData<Boolean>()
    private val _exceptionState = SingleLiveEvent<Unit>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _exceptionState.value = Unit
        _progressBarState.value = false
        Log.e(CONTACT_DETAILS_VIEW_MODEL_TAG, throwable.toString())
    }

    init {
        loadUserDetail(id)
    }

    private fun loadUserDetail(id: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            _progressBarState.value = true
            _user.value = contactsRepository.getFullContactDetails(id)
            _progressBarState.value = false
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted contactId: String): ContactDetailsViewModel
    }
}