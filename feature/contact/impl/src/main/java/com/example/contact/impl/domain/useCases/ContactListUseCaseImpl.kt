package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.repository.ContactsRepository
import com.example.entity.Contact
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactListUseCaseImpl @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ContactListUseCase {

    private var contacts = emptyList<Contact>()

    override suspend fun getContactList(): List<Contact> {
        contacts = contactsRepository.getShortContactsDetails()
        return contacts
    }

    override suspend fun searchContactByQuery(query: String): List<Contact> {
        val filteredList = ArrayList<Contact>()
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