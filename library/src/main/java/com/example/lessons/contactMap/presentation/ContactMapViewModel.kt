package com.example.lessons.contactMap.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.contacts.domain.entity.ContactMap
import com.example.lessons.utils.liveData.SingleLiveEvent
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class ContactMapViewModel @Inject constructor(
    private val contactMapUseCase: ContactMapUseCase
) : ViewModel() {

    val contactMapList: StateFlow<List<ContactMap>?> get() = _contactMapList.asStateFlow()
    val contactMap: LiveData<ContactMap?> get() = _contactMap
    val contactAddress: LiveData<Address?> get() = _contactAddress
    val fatalExceptionState: LiveData<Unit> get() = _fatalExceptionState
    val networkExceptionState: LiveData<Unit> get() = _networkExceptionState
    val serverExceptionState: LiveData<Unit> get() = _serverExceptionState
    private val _contactMapList = MutableStateFlow<List<ContactMap>?>(emptyList())
    private val _contactMap = MutableLiveData<ContactMap?>()
    private val _contactAddress = MutableLiveData<Address?>()
    private val _networkExceptionState = SingleLiveEvent<Unit>()
    private val _fatalExceptionState = SingleLiveEvent<Unit>()
    private val _serverExceptionState = SingleLiveEvent<Unit>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(CONTACT_MAP_VIEW_MODEL_TAG, throwable.toString())
    }

    fun fetchAddress(latitude: String, longitude: String) {
        viewModelScope.launch {
            when (val result = contactMapUseCase.getAddress(geocode = "$longitude,$latitude")) {
                is ApiResponse.Failure.HttpFailure -> _serverExceptionState.value = Unit
                is ApiResponse.Failure.NetworkFailure -> _networkExceptionState.value = Unit
                is ApiResponse.Failure.UnknownFailure -> _fatalExceptionState.value = Unit
                is ApiResponse.Success -> _contactAddress.value = result.data
            }
        }
    }

    fun updateContactMap(contactMap: ContactMap) {
        _contactMap.value = contactMap
        saveContactMap(contactMap)
    }

    fun getAllContactMaps() {
        contactMapUseCase.getAllContactMaps()
            .onEach(_contactMapList::emit)
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }

    fun getContactMapById(id: String) {
        viewModelScope.launch {
            _contactMap.value = contactMapUseCase.getContactMapById(id)
        }
    }

    fun deleteContactMap(id: String) {
        viewModelScope.launch {
            contactMapUseCase.deleteContactMap(id)
        }
    }

    private fun saveContactMap(contactMap: ContactMap) {
        viewModelScope.launch {
            contactMapUseCase.createContactMap(contactMap)
        }
    }

    private companion object {
        val CONTACT_MAP_VIEW_MODEL_TAG: String = ContactMapViewModel::class.java.simpleName
    }
}
