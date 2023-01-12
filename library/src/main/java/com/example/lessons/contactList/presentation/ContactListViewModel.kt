package com.example.lessons.contactList.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contacts.domain.contactList.useCases.ContactListUseCase
import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.utils.liveData.SingleLiveEvent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

internal class ContactListViewModel @Inject constructor(
    private val contactListUseCase: ContactListUseCase
) : ViewModel() {

    val users: LiveData<List<Contact>?> get() = _users
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    val exceptionState: LiveData<Unit> get() = _exceptionState
    private val _users = MutableLiveData<List<Contact>?>()
    private val _progressBarState = MutableLiveData<Boolean>()
    private val _exceptionState = SingleLiveEvent<Unit>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _exceptionState.value = Unit
        _progressBarState.value = false
        Log.e(CONTACT_LIST_VIEW_MODEL_TAG, throwable.toString())
    }

    init {
        loadUsers()
    }

    fun filterUsers(query: String) {
        viewModelScope.launch {
            _users.value = contactListUseCase.searchContactByQuery(query = query)
        }
    }

    private fun loadUsers() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _progressBarState.value = true
            _users.value = contactListUseCase.getContactList()
            _progressBarState.value = false
        }
    }

    private companion object {
        val CONTACT_LIST_VIEW_MODEL_TAG: String = ContactListViewModel::class.java.simpleName
    }
}
