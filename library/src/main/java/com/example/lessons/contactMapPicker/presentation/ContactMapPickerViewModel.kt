package com.example.lessons.contactMapPicker.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contactMapPicker.presentation.model.ContactMapPicker
import com.example.lessons.contactMapPicker.presentation.model.toContactMapPicker
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

internal class ContactMapPickerViewModel @Inject constructor(
    private val contactMapUseCase: ContactMapUseCase
) : ViewModel() {

    val contactMapPickerList: StateFlow<List<ContactMapPicker>?> get() = _contactMapPickerList.asStateFlow()
    val selectedList: ArrayList<String> = ArrayList()
    val dataValidation: LiveData<Unit> get() = _dataValidation
    private val _contactMapPickerList = MutableStateFlow<List<ContactMapPicker>?>(emptyList())
    private val _dataValidation = MutableLiveData<Unit>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(CONTACT_MAP_PICKER_VIEW_MODEL_TAG, throwable.toString())
    }

    private suspend fun getMapPickerFlow(): Flow<List<ContactMapPicker>> {
        return contactMapUseCase.getAllContactMaps()
            .map { contactMapList ->
                contactMapList.map { contactMap ->
                    contactMap.toContactMapPicker()
                }
            }
    }

    suspend fun getAllContactMaps() {
        getMapPickerFlow()
            .onEach(_contactMapPickerList::emit)
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }

    suspend fun changeItem(contactMapPickerId: String, isSelected: Boolean) {
        getMapPickerFlow().map { contactMapList ->
            contactMapList.map { contactMapPicker ->
                if (contactMapPicker.id == contactMapPickerId) {
                    contactMapPicker.isSelected = isSelected
                    selectedList.add(contactMapPickerId)
                    _dataValidation.value = Unit
                }
                return@map contactMapPicker
            }
        }
            .onEach(_contactMapPickerList::emit)
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }

    private companion object {
        val CONTACT_MAP_PICKER_VIEW_MODEL_TAG: String =
            ContactMapPickerViewModel::class.java.simpleName
    }
}