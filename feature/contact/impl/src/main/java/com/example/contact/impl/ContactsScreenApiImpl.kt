package com.example.contact.impl

import com.example.contact.api.screen.ContactsScreenApi
import com.example.contact.impl.presentation.details.ContactDetailsFragment
import com.example.contact.impl.presentation.list.ContactListFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

class ContactsScreenApiImpl @Inject constructor() : ContactsScreenApi {
    override fun getListScreen() = FragmentScreen { ContactListFragment() }

    override fun getDetailsScreen(contactId: Int) =
        FragmentScreen { ContactDetailsFragment.newInstance(contactId) }
}
