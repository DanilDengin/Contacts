package com.example.lessons.contactdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lessons.Contact
import com.example.lessons.repositories.ContactsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class ContactDetailsViewModel @AssistedInject constructor(
    @Assisted id: String,
    private val contactsRepository: ContactsRepository
) : ViewModel() {

    val user: LiveData<Contact> get() = _user
    private val _user = MutableLiveData<Contact>()
    private val compositeDisposable = CompositeDisposable()
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    private val _progressBarState = MutableLiveData<Boolean>()
    val exceptionState: LiveData<Unit> get() = _exceptionState
    private val _exceptionState = MutableLiveData<Unit>()

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
                    { _exceptionState.value = Unit })
        )
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted contactId: String): ContactDetailsViewModel
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }
}