package com.example.lessons.data.contacts.repository

import android.content.Context
import android.provider.ContactsContract
import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.contacts.domain.repository.ContactsRepository
import java.sql.Date
import java.util.GregorianCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl(private val context: Context) :
    ContactsRepository {

    override val contacts = ArrayList<Contact>()

    override suspend fun getShortContactsDetails(): List<Contact> {
        val contentUri = ContactsContract.Contacts.CONTENT_URI
        val idColumn = ContactsContract.Contacts._ID
        val displayName = ContactsContract.Contacts.DISPLAY_NAME
        val cursor = context.contentResolver.query(
            contentUri, null,
            null, null, "$displayName ASC"
        )
        withContext(Dispatchers.IO) {
            cursor.use {
                if (cursor != null) {
                    if (cursor.count > 0) {
                        while (cursor.moveToNext()) {
                            val id: String =
                                cursor.getString(cursor.getColumnIndexOrThrow(idColumn))
                            val name = cursor.getString(cursor.getColumnIndexOrThrow(displayName))
                            val numbers: List<String> = getNumbers(id)
                            if (numbers.isEmpty()) {
                                continue
                            }
                            val contact =
                                Contact(
                                    name = name,
                                    number1 = numbers[0],
                                    id = id
                                )
                            contacts.add(contact)
                        }
                    }
                }
            }
        }
        return contacts
    }

    override suspend fun getFullContactDetails(contactId: String): Contact? {
        val contentUri = ContactsContract.Contacts.CONTENT_URI
        val idColumn = ContactsContract.Contacts._ID
        val displayName = ContactsContract.Contacts.DISPLAY_NAME
        var contact: Contact? = null
        val cursor = context.contentResolver.query(
            contentUri, null,
            "$idColumn = $contactId", null, null
        )
        withContext(Dispatchers.IO) {
            cursor.use {
                if (cursor != null) {
                    cursor.moveToNext()
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(displayName))
                    val numbers = getNumbers(contactId)
                    val email = getEmail(contactId)
                    val birthday = getBirthday(contactId)
                    when {
                        numbers.size == NUMBER_ARRAY_LIST_SIZE_ONE_KEY -> {
                            contact = Contact(
                                name = name,
                                number1 = numbers[0],
                                email1 = email[0],
                                email2 = email[1],
                                birthday = birthday,
                                id = contactId
                            )
                        }
                        numbers.size > NUMBER_ARRAY_LIST_SIZE_ONE_KEY -> {
                            contact = Contact(
                                name = name,
                                number1 = numbers[0],
                                number2 = numbers[1],
                                email1 = email[0],
                                email2 = email[1],
                                birthday = birthday,
                                id = contactId
                            )
                        }
                    }
                }
            }
        }
        return contact
    }

    private fun getNumbers(contactId: String): List<String> {
        val phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneContactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val numberColumn = ContactsContract.CommonDataKinds.Phone.NUMBER
        val numbers = ArrayList<String>()
        val phoneCursor = context.contentResolver.query(
            phoneContentUri, null,
            "$phoneContactId = ?", arrayOf(contactId), null
        )

        phoneCursor.use {
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    val phoneNumber =
                        StringBuilder(
                            phoneCursor.getString(
                                phoneCursor.getColumnIndexOrThrow(
                                    numberColumn
                                )
                            )
                        )
                    if (phoneNumber[0] == '8') {
                        phoneNumber.replace(0, 1, "+7")
                    }
                    val number = phoneNumber
                        .toString()
                        .replace(" ", "")
                        .replace("-", "")
                    if (!numbers.contains(number)) {
                        numbers.add(number)
                    }
                }
            }
        }
        return numbers
    }

    private fun getEmail(contactId: String): Array<String?> {
        val number = ContactsContract.CommonDataKinds.Phone.NUMBER
        val emailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        val emailContactId = ContactsContract.CommonDataKinds.Email.CONTACT_ID
        val emails = arrayOfNulls<String>(2)
        var count = 0
        val emailCursor = context.contentResolver.query(
            emailContentUri, null,
            "$emailContactId = ?", arrayOf(contactId), null
        )

        emailCursor.use {
            if (emailCursor != null) {
                while (emailCursor.moveToNext()) {
                    emails[count] =
                        (emailCursor.getString(emailCursor.getColumnIndexOrThrow(number)))
                    count++
                }
            }
        }
        return emails
    }

    private fun getBirthday(contactId: String): GregorianCalendar? {
        val birthdayContentUri = ContactsContract.Data.CONTENT_URI
        val displayName = ContactsContract.Contacts.DISPLAY_NAME
        val startDate = ContactsContract.CommonDataKinds.Event.START_DATE
        var birthday: String? = null
        var birthdayDate: GregorianCalendar? = null
        val columns = arrayOf(
            ContactsContract.CommonDataKinds.Event.START_DATE
        )
        val where =
            ContactsContract.CommonDataKinds.Event.TYPE + "=" +
                ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY +
                " and " + ContactsContract.CommonDataKinds.Event.MIMETYPE + " = '" +
                ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE + "' and " +
                ContactsContract.Data.CONTACT_ID + " = " + contactId
        val birthdayCursor = context.contentResolver.query(
            birthdayContentUri, columns,
            where, null, displayName
        )

        birthdayCursor.use {
            if (birthdayCursor != null) {
                while (birthdayCursor.moveToNext())
                    birthday =
                        birthdayCursor.getString(birthdayCursor.getColumnIndexOrThrow(startDate))
            }
        }
        if (birthday != null) {
            birthdayDate = GregorianCalendar()
            birthdayDate.time = Date.valueOf(birthday)
        }
        return birthdayDate
    }

    private companion object {
        const val NUMBER_ARRAY_LIST_SIZE_ONE_KEY = 1
    }
}