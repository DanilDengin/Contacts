package com.example.lessons.contactlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactListViewModel(private val contactsRepository: ContactsRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    val users: LiveData<List<Contact>?> get() = _users
    private val _users = MutableLiveData<List<Contact>?>()
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    private val _progressBarState = MutableLiveData<Boolean>()
    val exceptionState: LiveData<Boolean> get() = _exceptionState
    private val _exceptionState = MutableLiveData<Boolean>()

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
                    { _exceptionState.value = true })
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