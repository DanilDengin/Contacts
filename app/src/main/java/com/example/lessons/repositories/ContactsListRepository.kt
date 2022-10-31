package com.example.lessons.repositories

import android.content.Context
import android.provider.ContactsContract
import com.example.lessons.Contact

open class ContactsListRepository {

    fun getShortContactsDetails(context: Context): List<Contact> {
        val contentUri = ContactsContract.Contacts.CONTENT_URI
        val idColumn = ContactsContract.Contacts._ID
        val displayName = ContactsContract.Contacts.DISPLAY_NAME
        val contacts = ArrayList<Contact>()
        val cursor = context.contentResolver.query(
            contentUri, null,
            null, null, null
        )

        cursor.use {
            if (cursor != null) {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id: String = cursor.getString(cursor.getColumnIndexOrThrow(idColumn))
                        val name = cursor.getString(cursor.getColumnIndexOrThrow(displayName))
                        val numbers: List<String> = getNumbers(id, context)
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
        return contacts
    }

    protected fun getNumbers(contact_id: String, context: Context): List<String> {
        val phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneContactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val numberColumn = ContactsContract.CommonDataKinds.Phone.NUMBER
        val numbers = ArrayList<String>()
        val phoneCursor = context.contentResolver.query(
            phoneContentUri, null,
            "$phoneContactId = ?", arrayOf(contact_id), null
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
}