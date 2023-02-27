package com.example.contact.impl.domain.useCases

import com.example.common.address.domain.local.repository.ContactMapRepository
import com.example.contact.impl.data.model.ContactPhoneDb
import com.example.contact.impl.domain.entity.ContactDetails
import com.example.contact.impl.domain.repository.ContactsRepository
import com.example.contact.impl.domain.time.CurrentTime
import java.text.DecimalFormat
import java.util.Calendar
import java.util.StringJoiner
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ContactDetailsUseCaseImpl @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val contactMapRepository: ContactMapRepository,
    private val currentTime: CurrentTime
) : ContactDetailsUseCase {

    private var contact: ContactPhoneDb? = null

    override suspend fun getContactDetailsById(id: String): ContactDetails? {
        contact = contactsRepository.getFullContactDetails(id)
        val contactMap = contactMapRepository.getContactMapById(id)
        val birthday = contact?.birthday?.let { birthdayDate ->
            val data = StringJoiner(".")
            val format = DecimalFormat("00")
            data.add(format.format(birthdayDate.get(Calendar.DAY_OF_MONTH)))
                .add(format.format(birthdayDate.get(Calendar.MONTH) + 1))
                .add(format.format(birthdayDate.get(Calendar.YEAR)))
                .toString()
        }
        return contact?.let { contactPhone ->
            ContactDetails(
                name = contactPhone.name,
                numberPrimary = contactPhone.numberPrimary,
                numberSecondary = contactPhone.numberSecondary,
                emailPrimary = contactPhone.emailPrimary,
                emailSecondary = contactPhone.emailSecondary,
                address = contactMap?.address,
                birthday = birthday,
                id = contactPhone.id
            )
        }
    }

    override suspend fun getAlarmDate(): Long {
        return withContext(Dispatchers.Default) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentTime.getCurrentTime()
            getContact()?.birthday?.also { birthday ->
                if (calendar[Calendar.DAY_OF_YEAR] > birthday.get(Calendar.DAY_OF_YEAR)) {
                    calendar.add(Calendar.YEAR, 1)
                }
                if (birthday.get(Calendar.MONTH) == Calendar.FEBRUARY &&
                    birthday.get(Calendar.DAY_OF_MONTH) == TWENTY_NINE_MONTH_DAY
                ) {
                    birthday.set(Calendar.DAY_OF_MONTH, TWENTY_EIGHT_MONTH_DAY)
                }
                calendar[Calendar.MILLISECOND] = 0
                calendar[Calendar.SECOND] = 0
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.DAY_OF_MONTH] = birthday.get(Calendar.DAY_OF_MONTH)
                calendar[Calendar.MONTH] = birthday.get(Calendar.MONTH)
            }
            calendar.timeInMillis
        }
    }

    private fun getContact() = contact

    private companion object {
        const val TWENTY_NINE_MONTH_DAY = 29
        const val TWENTY_EIGHT_MONTH_DAY = 28
    }
}