package com.example.lessons.contactMap.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contacts.domain.contactMap.QueryState
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCaseImpl
import com.example.lessons.contacts.domain.entity.Address
import com.example.lessons.utils.liveData.SingleLiveEvent
import javax.inject.Inject
import kotlinx.coroutines.launch

internal class ContactMapViewModel @Inject constructor(
//    private val addressRepository: AddressRepositoryImpl
private val contactMapUseCaseImpl: ContactMapUseCaseImpl
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
            QueryState.SUCCESS
            when (contactMapUseCaseImpl.getQueryState(geocode = "$longitude,$latitude")) {
                QueryState.SUCCESS -> {
                    _contactAddress.value = contactMapUseCaseImpl.getData()
                }
                QueryState.NETWORK_ERROR -> _networkExceptionState.value = Unit
                QueryState.SERVICE_ERROR -> _serverExceptionState.value = Unit
                QueryState.UNKNOWN_ERROR -> _fatalExceptionState.value = Unit
            }
        }
    }
}