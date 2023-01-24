package com.example.lessons.contactMap.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contacts.domain.api.response.ApiResponse
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.utils.liveData.SingleLiveEvent
import javax.inject.Inject
import kotlinx.coroutines.launch

internal class ContactMapViewModel @Inject constructor(
    private val contactMapUseCase: ContactMapUseCase
) : ViewModel() {

    val contactAddress: LiveData<Address?> get() = _contactAddress
    val fatalExceptionState: LiveData<Unit> get() = _fatalExceptionState
    val networkExceptionState: LiveData<Unit> get() = _networkExceptionState
    val serverExceptionState: LiveData<Unit> get() = _serverExceptionState
    private val _contactAddress = MutableLiveData<Address?>()
    private val _networkExceptionState = SingleLiveEvent<Unit>()
    private val _fatalExceptionState = SingleLiveEvent<Unit>()
    private val _serverExceptionState = SingleLiveEvent<Unit>()

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
}
