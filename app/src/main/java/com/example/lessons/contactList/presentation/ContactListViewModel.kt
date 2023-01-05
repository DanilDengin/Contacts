package com.example.lessons.contactList.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.data.contacts.repository.ContactsRepositoryImpl
import com.example.lessons.domain.contacts.entity.Contact
import com.example.lessons.utils.liveData.SingleLiveEvent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepositoryImpl
) : ViewModel() {

    private companion object {
        val CONTACT_LIST_VIEW_MODEL_TAG: String = ContactListViewModel::class.java.simpleName
    }

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

    private fun loadUsers() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _progressBarState.value = true
            _users.value = contactsRepository.getShortContactsDetails()
            _progressBarState.value = false
        }
    }

    fun filterUsers(query: String?) {
        if (query == null || query.isBlank()) {
            loadUsers()
        } else {
            viewModelScope.launch {
                val filteredList = ArrayList<Contact>()
                withContext(Dispatchers.Default) {
                    val trimmedQuery = query.trim()
                    users.value?.forEach { user ->
                        if (user.name.contains(trimmedQuery, ignoreCase = true)) {
                            filteredList.add(user)
                        }
                    }
                }
                _users.value = filteredList
            }
        }
    }
}