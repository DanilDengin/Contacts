package com.example.lessons

import androidx.lifecycle.ViewModel
import com.example.contact.api.screen.ContactsScreenApi
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

internal class MainActivityViewModel @Inject constructor(
    private val router: Router,
    private val contactsScreenApi: ContactsScreenApi
) : ViewModel() {

    fun navigateToListFragment() {
        router.newRootScreen(contactsScreenApi.getListScreen())
    }

    fun navigateToBirthdayContact(contactId: Int) {
        router.navigateTo(contactsScreenApi.getDetailsScreen(contactId))
    }
}
