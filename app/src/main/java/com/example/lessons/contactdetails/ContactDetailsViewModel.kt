package com.example.lessons.contactdetails

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.R
import com.example.lessons.repositories.ContactsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactDetailsViewModel(id: String, context: Context) : ViewModel() {

    private val contactsRepository = ContactsRepository()
    private val user = MutableLiveData<Contact>()
    private val compositeDisposable = CompositeDisposable()
    private val progressBarState = MutableLiveData<Boolean>()

    init {
        loadUserDetail(id, context)
    }

    fun getUserDetails(): LiveData<Contact> {
        return user
    }

    fun getProgressBarState(): LiveData<Boolean> {
        return progressBarState
    }

    private fun loadUserDetail(id: String, context: Context) {
        compositeDisposable.add(
            Single.fromCallable { contactsRepository.getFullContactDetails(id, context) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { progressBarState.value = true }
                .doOnTerminate { progressBarState.value = false }
                .subscribe({ contact -> user.value = contact },
                    {
                        Toast.makeText(
                            context,
                            context.getText(R.string.exception),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}