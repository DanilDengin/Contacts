package com.example.lessons.contactlist

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.R
import com.example.lessons.repositories.ContactsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactListViewModel(context: Context) : ViewModel() {
    private val contactsRepository = ContactsRepository()
    private val users = MutableLiveData<List<Contact>?>()
    private val compositeDisposable = CompositeDisposable()
    private val progressBarState = MutableLiveData<Boolean>()


    init {
        loadUsers(context)
    }

    fun getUsers(): LiveData<List<Contact>?> {
        return users
    }

    fun getProgressBarState(): LiveData<Boolean> {
        return progressBarState
    }

    private fun loadUsers(context: Context) {
        compositeDisposable.add(
            Single.fromCallable { contactsRepository.getShortContactsDetails(context) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBarState.value = true }
                .doOnTerminate { progressBarState.value = false }
                .subscribe({ contacts -> users.value = contacts },
                    {
                        Toast.makeText(
                            context,
                            context.getText(R.string.exception),
                            Toast.LENGTH_LONG
                        ).show()
                    })
        )
    }

    fun filterUsers(query: String?, context: Context) {
        if (query == null || query.isBlank()) {
            loadUsers(context = context)
        } else {
            val trimmedQuery = query.trim()
            users.value?.let { contactList ->
                Observable.fromIterable(contactList)
                    .filter { contact -> contact.name.contains(trimmedQuery, ignoreCase = true) }
                    .toList()
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { filteredList -> users.value = filteredList }
            }
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}