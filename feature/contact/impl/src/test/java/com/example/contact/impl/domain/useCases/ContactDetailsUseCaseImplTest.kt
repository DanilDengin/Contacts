package com.example.contact.impl.domain.useCases

import com.example.contact.impl.domain.repository.ContactsRepository
import com.example.contact.impl.domain.time.CurrentTimeImpl
import com.example.entity.Contact
import com.example.impl.contacts.domain.currentDateTest
import com.example.impl.contacts.domain.danil
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import java.util.Calendar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class ContactDetailsUseCaseImplTest {

    private val contactsRepository = mockk<ContactsRepository>()

    private val currentTime = mockk<CurrentTimeImpl>()

    private lateinit var contactDetailsUseCaseImpl: ContactDetailsUseCaseImpl

    @BeforeEach
    fun setUp() {
        contactDetailsUseCaseImpl = ContactDetailsUseCaseImpl(contactsRepository, currentTime)
    }

    @Test
    fun `should return the same contact as in repository`() = runTest {
        coEvery { contactsRepository.getFullContactDetails(ofType()) } returns danil
        val actual: Contact? = contactDetailsUseCaseImpl.getContactById("1")
        coVerify(exactly = 1) { contactsRepository.getFullContactDetails(any()) }
        val expected = danil
        assertEquals(expected, actual)
    }

    @Test
    fun `should return the same birthday time as function`() = runTest {
        val mock = spyk(contactDetailsUseCaseImpl, recordPrivateCalls = true)
        every { mock["getContact"]() } returns danil
        every { currentTime.getCurrentTime() } returns currentDateTest.timeInMillis
        val actual = mock.getAlarmDate()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentDateTest.timeInMillis
        danil.birthday?.also {
            calendar[Calendar.MILLISECOND] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.DAY_OF_MONTH] = it.get(Calendar.DAY_OF_MONTH)
            calendar[Calendar.MONTH] = it.get(Calendar.MONTH)
        }
        coVerify(exactly = 1) { currentTime.getCurrentTime() }
        val expected = calendar.timeInMillis
        assertEquals(expected, actual)
    }
}