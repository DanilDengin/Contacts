package com.example.impl.contacts

import com.example.api.contacts.ContactsScreenApi
import com.example.impl.contacts.presentation.details.ContactDetailsFragment
import com.example.impl.contacts.presentation.list.ContactListFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

class ContactsScreenApiImpl @Inject constructor() : ContactsScreenApi {
    override fun getListScreen() = FragmentScreen { ContactListFragment() }

    override fun getDetailsScreen(contactId: Int) =
        FragmentScreen { ContactDetailsFragment.newInstance(contactId) }
}