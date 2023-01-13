package com.example.lessons.contacts.domain.contactList.useCases

import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.contacts.domain.repository.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactListUseCaseImpl(
    private val contactsRepository: ContactsRepository
) : ContactListUseCase {

    var contacts = emptyList<Contact>()

    override suspend fun getContactList(): List<Contact> {
        contacts = contactsRepository.getShortContactsDetails()
        return contacts
    }

    override suspend fun searchContactByQuery(query: String): List<Contact> {
        val filteredList = ArrayList<Contact>()
        withContext(Dispatchers.Default) {
            val trimmedQuery = query.trim()
            contacts.forEach { user ->
                if (user.name.contains(trimmedQuery, ignoreCase = true)) {
                    filteredList.add(user)
                }
            }
        }
        return filteredList
    }
}