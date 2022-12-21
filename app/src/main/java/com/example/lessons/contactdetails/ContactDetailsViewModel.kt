package com.example.lessons.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ContactDetailsViewModel(
    id: String,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val user: LiveData<Contact> get() = _user
    private val _user = MutableLiveData<Contact>()
    private val compositeDisposable = CompositeDisposable()
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    private val _progressBarState = MutableLiveData<Boolean>()
    val exceptionState: LiveData<Boolean> get() = _exceptionState
    private val _exceptionState = MutableLiveData<Boolean>()

    init {
        loadUserDetail(id)
    }

    private fun loadUserDetail(id: String) {
        compositeDisposable.add(
            Single.fromCallable {
                requireNotNull(contactsRepository.getFullContactDetails(id))
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { _progressBarState.value = true }
                .doOnTerminate { _progressBarState.value = false }
                .subscribe({ contact -> _user.value = contact },
                    { _exceptionState.value = true })
        )
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}