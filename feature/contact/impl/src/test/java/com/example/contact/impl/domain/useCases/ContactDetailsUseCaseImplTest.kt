package com.example.contact.impl.domain.useCases

import com.example.common.address.domain.local.repository.ContactMapRepository
import com.example.contact.impl.domain.currentDateTest
import com.example.contact.impl.domain.danilContactMap
import com.example.contact.impl.domain.danilDetails
import com.example.contact.impl.domain.danilPhone
import com.example.contact.impl.domain.entity.ContactDetails
import com.example.contact.impl.domain.repository.ContactsRepository
import com.example.contact.impl.domain.time.CurrentTimeImpl
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

    private val contactMapRepository = mockk<ContactMapRepository>()

    private lateinit var contactDetailsUseCaseImpl: ContactDetailsUseCaseImpl

    @BeforeEach
    fun setUp() {
        contactDetailsUseCaseImpl =
            ContactDetailsUseCaseImpl(contactsRepository, contactMapRepository, currentTime)
    }

    @Test
    fun `should return the same contact as in repository`() = runTest {
        coEvery { contactsRepository.getFullContactDetails(ofType()) } returns danilPhone
        coEvery { contactMapRepository.getContactMapById("1") } returns danilContactMap
        val actual: ContactDetails? = contactDetailsUseCaseImpl.getContactDetailsById("1")
        coVerify(exactly = 1) { contactsRepository.getFullContactDetails(any()) }
        val expected = danilDetails
        assertEquals(expected, actual)
    }

    @Test
    fun `should return the same birthday time as function`() = runTest {
        val mock = spyk(contactDetailsUseCaseImpl, recordPrivateCalls = true)
        every { mock["getContact"]() } returns danilPhone
        every { currentTime.getCurrentTime() } returns currentDateTest.timeInMillis
        val actual = mock.getAlarmDate()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentDateTest.timeInMillis
        danilPhone.birthday?.also {
            calendar[Calendar.MILLISECOND] = 0
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MINUTE] = 0
            calendar[Calendar.HOUR_OF_DAY] = 0
            calendar[Calendar.DAY_OF_MONTH] = it.get(Calendar.DAY_OF_MONTH)
            calendar[Calendar.MONTH] = it.get(Calendar.MONTH) - 1
        }
        coVerify(exactly = 1) { currentTime.getCurrentTime() }
        val expected = calendar.timeInMillis
        assertEquals(expected, actual)
    }
}