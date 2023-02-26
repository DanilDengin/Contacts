package com.example.contact.impl.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.map.screen.MapScreenApi
import com.example.contact.impl.domain.entity.ContactDetails
import com.example.contact.impl.domain.mappers.toArguments
import com.example.contact.impl.domain.useCases.ContactDetailsUseCase
import com.example.utils.liveData.SingleLiveEvent
import com.example.utils.tag.tagObj
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal class ContactDetailsViewModel @AssistedInject constructor(
    @Assisted id: String,
    private val contactDetailsUseCase: ContactDetailsUseCase,
    private val router: Router,
    private val mapScreenApi: MapScreenApi
) : ViewModel() {

    val user: StateFlow<ContactDetails?> get() = _user.asStateFlow()
    val exceptionState: LiveData<Unit> get() = _exceptionState
    private val _user = MutableStateFlow<ContactDetails?>(null)
    private val _exceptionState = SingleLiveEvent<Unit>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _exceptionState.value = Unit
        Log.e(CONTACT_DETAILS_VIEW_MODEL_TAG, throwable.toString())
    }

    init {
        loadUserDetail(id)
    }

    suspend fun getAlarmDate(): Long {
        return contactDetailsUseCase.getAlarmDate()
    }

    fun navigateToMapFragment() {
        router.navigateTo(mapScreenApi.getMapScreen(user.value?.toArguments()))
    }

    private fun loadUserDetail(id: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            contactDetailsUseCase.getContactDetailsById(id)
                .collect(_user::emit)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted contactId: String): ContactDetailsViewModel
    }

    private companion object {
        val CONTACT_DETAILS_VIEW_MODEL_TAG: String = ContactDetailsViewModel.tagObj()
    }
}