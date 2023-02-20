package com.example.contact.impl.presentation.list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.map.MapScreenApi
import com.example.contact.impl.domain.useCases.ContactListUseCase
import com.example.contact.impl.presentation.details.ContactDetailsFragment
import com.example.themePicker.api.ThemeScreenApi
import com.example.utils.liveData.SingleLiveEvent
import com.example.utils.tag.tagObj
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ContactListViewModel @Inject constructor(
    private val contactListUseCase: ContactListUseCase,
    private val router: Router,
    private val themeScreenApi: ThemeScreenApi,
    private val mapScreenApi: MapScreenApi
) : ViewModel() {
    val users: LiveData<List<com.example.entity.Contact>?> get() = _users
    val progressBarState: LiveData<Boolean> get() = _progressBarState
    val exceptionState: LiveData<Unit> get() = _exceptionState
    private val _users = MutableLiveData<List<com.example.entity.Contact>?>()
    private val _progressBarState = MutableLiveData<Boolean>()
    private val _exceptionState = SingleLiveEvent<Unit>()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _exceptionState.value = Unit
        _progressBarState.value = false
        Log.e(CONTACT_LIST_VIEW_MODEL_TAG, throwable.toString())
    }

    init {
        loadUsers()
    }

    fun filterUsers(query: String) {
        viewModelScope.launch {
            _users.value = contactListUseCase.searchContactByQuery(query = query)
        }
    }

    fun navigateToDetails(id: String) {
        router.navigateTo(FragmentScreen { ContactDetailsFragment.newInstance(id.toInt()) })
    }

    fun navigateToThemePickerFragment() {
        router.navigateTo(themeScreenApi.getThemeScreen())
    }

    fun navigateToMapFragment() {
        router.navigateTo(mapScreenApi.getMapScreen(null))
    }

    private fun loadUsers() {
        viewModelScope.launch(coroutineExceptionHandler) {
            _progressBarState.value = true
            _users.value = contactListUseCase.getContactList()
            _progressBarState.value = false
        }
    }

    private companion object {
        val CONTACT_LIST_VIEW_MODEL_TAG: String = ContactListViewModel.tagObj()
    }
}
