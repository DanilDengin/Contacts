package com.example.lessons.contacts.domain.contactDetails.useCases

import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.contacts.domain.repository.ContactsRepository
import java.util.Calendar
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactDetailsUseCaseImpl (
    private val contactsRepository: ContactsRepository
) : ContactDetailsUseCase {

    override suspend fun getContactById(id: String): Contact? {
        return contactsRepository.getFullContactDetails(id)
    }

    override suspend fun getAlarmDate(): Long {
        val calendar = Calendar.getInstance()
        withContext(Dispatchers.Default) {
            calendar.timeInMillis = System.currentTimeMillis()
            contactsRepository.contact?.birthday?.also { birthday ->
                if (calendar[Calendar.DAY_OF_YEAR] > birthday.get(Calendar.DAY_OF_YEAR)) {
                    calendar.add(Calendar.YEAR, 1)
                }
                if (birthday.get(Calendar.MONTH) == Calendar.FEBRUARY &&
                    birthday.get(Calendar.DAY_OF_MONTH) == TWENTY_NINE_MONTH_DAY
                ) {
                    birthday.set(Calendar.DAY_OF_MONTH, TWENTY_EIGHT_MONTH_DAY)
                }
                calendar[Calendar.MINUTE] = 0
                calendar[Calendar.HOUR_OF_DAY] = 0
                calendar[Calendar.DAY_OF_MONTH] = birthday.get(Calendar.DAY_OF_MONTH)
                calendar[Calendar.MONTH] = birthday.get(Calendar.MONTH)
            }
        }
        return calendar.timeInMillis

    }

    private companion object {
        const val TWENTY_NINE_MONTH_DAY = 29
        const val TWENTY_EIGHT_MONTH_DAY = 28
    }
}