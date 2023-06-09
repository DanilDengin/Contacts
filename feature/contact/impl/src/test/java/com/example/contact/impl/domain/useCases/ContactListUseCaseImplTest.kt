package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.contactSortedListTest
import com.example.contact.impl.domain.contactsListTest
import com.example.contact.impl.domain.contactsPhoneTest
import com.example.contact.impl.domain.entity.ContactDetails
import com.example.contact.impl.domain.entity.ContactList
import com.example.contact.impl.domain.repository.ContactsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ContactListUseCaseImplTest {

    private val contactRepository = mockk<ContactsRepository>()

    private lateinit var contactListUseCaseImpl: ContactListUseCase

    private var actual: List<ContactList>? = null

    @BeforeEach
    fun setUp() {
        actual = null
        contactListUseCaseImpl = ContactListUseCaseImpl(contactRepository)
    }

    @Test
    fun `should return the same list of contacts as in repository`() = runTest {
        coEvery { contactRepository.getShortContactsDetails() } returns contactsPhoneTest
        actual = contactListUseCaseImpl.getContactList()
        coVerify(exactly = 1) { contactRepository.getShortContactsDetails() }
        val expected: List<ContactList> = contactsListTest
        assertEquals(expected, actual)
    }

    @Test
    fun `should return valid query result`() = runTest {
        coEvery { contactRepository.getShortContactsDetails() } returns contactsPhoneTest
        contactListUseCaseImpl.getContactList()
        actual = contactListUseCaseImpl.searchContactByQuery("d")
        val expected = contactSortedListTest
        assertEquals(expected, actual)
    }

    @Test
    fun `should return empty query result`() = runTest {
        coEvery { contactRepository.getShortContactsDetails() } returns contactsPhoneTest
        contactListUseCaseImpl.getContactList()
        actual = contactListUseCaseImpl.searchContactByQuery("freferd")
        val expected = emptyList<ContactDetails>()
        assertEquals(expected, actual)
    }

    @Test
    fun `should return full contact list query result`() = runTest {
        coEvery { contactRepository.getShortContactsDetails() } returns contactsPhoneTest
        contactListUseCaseImpl.getContactList()
        actual = contactListUseCaseImpl.searchContactByQuery("         ")
        val expected = contactsListTest
        assertEquals(expected, actual)
    }
}
