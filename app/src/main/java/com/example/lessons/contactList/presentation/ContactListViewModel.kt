package com.example.lessons.contactList.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.domain.contacts.entity.Contact
import com.example.lessons.utils.liveData.SingleLiveEvent
import com.example.lessons.data.contacts.repository.ContactsRepositoryImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ContactListViewModel @Inject constructor(
    private val contactsRepository: ContactsRepositoryImpl
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val users: LiveData<List<Contact>?> get() = _users
    private val _users = MutableLiveData<List<Contact>?>()
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    private val _progressBarState = MutableLiveData<Boolean>()
    val exceptionState : LiveData<Unit> get() = _exceptionState
    private val _exceptionState = SingleLiveEvent<Unit>()


    init {
        loadUsers()
    }

    private fun loadUsers() {
        compositeDisposable.add(
            Single.fromCallable { contactsRepository.getShortContactsDetails() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _progressBarState.value = true }
                .doOnTerminate { _progressBarState.value = false }
                .subscribe({ contacts -> _users.value = contacts },
                    { _exceptionState.value = Unit })
        )
    }

    fun filterUsers(query: String?) {
        if (query == null || query.isBlank()) {
            loadUsers()
        } else {
            val trimmedQuery = query.trim()
            users.value?.let { contactList ->
                Observable.fromIterable(contactList)
                    .filter { contact -> contact.name.contains(trimmedQuery, ignoreCase = true) }
                    .toList()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { filteredList -> _users.value = filteredList }
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}