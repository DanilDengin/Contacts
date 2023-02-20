package com.example.impl.contacts.domain.useCases

import com.example.entity.Contact
import com.example.impl.contacts.domain.repository.ContactsRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactListUseCaseImpl @Inject constructor(
    private val contactsRepository: ContactsRepository
) : ContactListUseCase {

    private var contacts = emptyList<com.example.entity.Contact>()

    override suspend fun getContactList(): List<com.example.entity.Contact> {
        contacts = contactsRepository.getShortContactsDetails()
        return contacts
    }

    override suspend fun searchContactByQuery(query: String): List<com.example.entity.Contact> {
        val filteredList = ArrayList<com.example.entity.Contact>()
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

    private fun getContacts()= contacts
}