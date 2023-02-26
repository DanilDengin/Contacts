package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.entity.ContactList
import com.example.contact.impl.domain.mappers.toContactList
import com.example.contact.impl.domain.repository.ContactsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ContactListUseCaseImpl @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ContactListUseCase {

    private var contacts = emptyList<ContactList>()

    override suspend fun getContactList(): List<ContactList> {
        contacts =
            contactsRepository.getShortContactsDetails().map { contactPhoneDb ->
                contactPhoneDb.toContactList()
            }
        return contacts
    }

    override suspend fun searchContactByQuery(query: String): List<ContactList> {
        val filteredList = ArrayList<ContactList>()
        withContext(Dispatchers.Default) {
            val trimmedQuery = query.trim()
            getContacts().forEach { user ->
                if (user.name.contains(trimmedQuery, ignoreCase = true)) {
                    filteredList.add(user)
                }
            }
        }
        return filteredList
    }

    private fun getContacts() = contacts
}