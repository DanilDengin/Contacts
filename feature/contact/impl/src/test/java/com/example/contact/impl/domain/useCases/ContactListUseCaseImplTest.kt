package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.repository.ContactsRepository
import com.example.entity.Contact
import com.example.impl.contacts.domain.contactListTest
import com.example.impl.contacts.domain.contactSortedListTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ContactListUseCaseImplTest {

    private val contactRepository = mockk<ContactsRepository>()

    private lateinit var contactListUseCaseImpl: ContactListUseCase

    private var actual: List<Contact>? = null

    @BeforeEach
    fun setUp() {
        actual = null
        contactListUseCaseImpl = ContactListUseCaseImpl(contactRepository)
    }

    @Test
    fun `should return the same list of contacts as in repository`() = runTest {
        coEvery { contactRepository.getShortContactsDetails() } returns contactListTest
        actual = contactListUseCaseImpl.getContactList()
        coVerify(exactly = 1) { contactListUseCaseImpl.getContactList() }
        val expected: List<Contact> = contactListTest
        assertEquals(expected, actual)
    }

    @Test
    fun `should return valid query result`() = runTest {
        val mock = spyk(contactListUseCaseImpl, recordPrivateCalls = true)
        every { mock["getContacts"]() } returns contactListTest
        actual = mock.searchContactByQuery("d")
        val expected = contactSortedListTest
        assertEquals(expected, actual)
    }

    @Test
    fun `should return empty query result`() = runTest {
        val mock = spyk(contactListUseCaseImpl, recordPrivateCalls = true)
        every { mock["getContacts"]() } returns contactListTest
        actual = mock.searchContactByQuery("freferd")
        val expected = emptyList<Contact>()
        assertEquals(expected, actual)
    }

    @Test
    fun `should return full contact list query result`() = runTest {
        val mock = spyk(contactListUseCaseImpl, recordPrivateCalls = true)
        every { mock["getContacts"]() } returns contactListTest
        actual = mock.searchContactByQuery("         ")
        val expected = contactListTest
        assertEquals(expected, actual)
    }
}

