package com.example.lessons.contactMap.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.utils.liveData.SingleLiveEvent
import javax.inject.Inject
import kotlinx.coroutines.launch

internal class ContactMapViewModel @Inject constructor(
    private val contactMapUseCase: ContactMapUseCase
) : ViewModel() {

    val contactAddress: LiveData<Address?> get() = _contactAddress
    val networkExceptionState: LiveData<Unit> get() = _networkExceptionState
    private val _contactAddress = MutableLiveData<Address?>()
    private val _networkExceptionState = SingleLiveEvent<Unit>()

    fun fetchAddress(latitude: String, longitude: String) {
        viewModelScope.launch {
            val result = contactMapUseCase.getAddress(geocode = "$longitude,$latitude")
            if (result.isSuccess) {
                _contactAddress.value = result.getOrNull()
            } else {
                _networkExceptionState.value = Unit
            }
        }
    }
}
