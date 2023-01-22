package com.example.lessons.contactMap.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contactMap.data.address.remote.model.AddressItem
import com.example.lessons.contactMap.data.address.remote.repository.AddressRepository
import com.example.lessons.utils.liveData.SingleLiveEvent
import com.example.lessons.utils.response.ApiResponse
import javax.inject.Inject
import kotlinx.coroutines.launch

internal class ContactMapViewModel @Inject constructor(
    private val addressRepository: AddressRepository
) : ViewModel() {

    val addressItem: LiveData<AddressItem?> get() = _addressItem
    val fatalExceptionState: LiveData<Unit> get() = _fatalExceptionState
    val networkExceptionState: LiveData<Unit> get() = _networkExceptionState
    private val _addressItem = MutableLiveData<AddressItem?>()
    private val _networkExceptionState = SingleLiveEvent<Unit>()
    private val _fatalExceptionState = SingleLiveEvent<Unit>()

    fun fetchAddress(latitude: String, longitude: String) {
        viewModelScope.launch {
            when (val response = addressRepository.getAddress(geocode = "$longitude,$latitude")) {
                is ApiResponse.Success -> _addressItem.value = response.data
                is ApiResponse.Failure.NetworkFailure -> _networkExceptionState.value = Unit
                else -> _fatalExceptionState.value = Unit
            }
        }
    }
}