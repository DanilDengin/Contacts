package com.example.lessons.contactMapPicker.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lessons.contactMapPicker.data.model.ContactMapPicker
import com.example.lessons.contactMapPicker.data.model.toContactMapPicker
import com.example.lessons.contacts.domain.contactMap.useCases.ContactMapUseCase
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

internal class ContactMapPickerViewModel @Inject constructor(
    private val contactMapUseCase: ContactMapUseCase
) : ViewModel() {

    val contactMapPickerList: StateFlow<List<ContactMapPicker>?> get() = _contactMapPickerList.asStateFlow()
    val selectedContactList = ArrayList<ContactMapPicker>()
    private val _contactMapPickerList = MutableStateFlow<List<ContactMapPicker>?>(emptyList())
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.e(CONTACT_MAP_PICKER_VIEW_MODEL_TAG, throwable.toString())
    }

    fun getAllContactMaps() {
        contactMapUseCase.getAllContactMaps()
            .map { contactMapList ->
                contactMapList.map { contactMap ->
                    contactMap.toContactMapPicker()
                }
            }
            .onEach(_contactMapPickerList::emit)
            .launchIn(viewModelScope + coroutineExceptionHandler)
    }

    fun changeContactSelection(contactMapPicker: ContactMapPicker, isSelected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (isSelected) {
                checkSelectedList()
            }
            contactMapPickerList.value?.map { contactMap ->
                var newContact = contactMap
                if (contactMap.id == contactMapPicker.id) {
                    newContact = ContactMapPicker(
                        contactMapPicker.name,
                        contactMapPicker.address,
                        contactMapPicker.id,
                        isSelected
                    )
                    if (isSelected) {
                        selectedContactList.add(newContact)
                    } else {
                        selectedContactList.remove(contactMapPicker)
                    }
                }
                return@map newContact
            }?.toList()
                .also { contactMapPickerList ->
                    _contactMapPickerList.emit(contactMapPickerList)
                }
        }
    }

    private suspend fun checkSelectedList() {
        if (selectedContactList.size == SELECT_LIST_ALLOWED_SIZE) {
            val contactId = selectedContactList[0].id
            contactMapPickerList.value?.map { contactMap ->
                var newContact = contactMap
                if (contactMap.id == contactId) {
                    newContact = ContactMapPicker(
                        contactMap.name,
                        contactMap.address,
                        contactMap.id,
                        !contactMap.isSelected
                    )
                    selectedContactList.removeAt(0)
                }
                return@map newContact
            }?.toList()
                .also { contactMapPickerList ->
                    _contactMapPickerList.emit(contactMapPickerList)
                }
        }
    }

    companion object {
        const val SELECT_LIST_ALLOWED_SIZE = 2
        private val CONTACT_MAP_PICKER_VIEW_MODEL_TAG: String =
            ContactMapPickerViewModel::class.java.simpleName
    }
}