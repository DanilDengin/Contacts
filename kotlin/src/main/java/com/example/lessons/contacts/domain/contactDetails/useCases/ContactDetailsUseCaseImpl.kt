package com.example.lessons.contacts.domain.contactDetails.useCases

import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.contacts.domain.repository.local.ContactsRepository
import com.example.lessons.contacts.domain.utils.time.CurrentTime
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactDetailsUseCaseImpl @Inject constructor(
    private val contactsRepository: ContactsRepository,
    private val currentTime: CurrentTime
) : ContactDetailsUseCase {

    private var contact: Contact? = null

    override suspend fun getContactById(id: String): Contact? {
        contact = contactsRepository.getFullContactDetails(id)
        return contact
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