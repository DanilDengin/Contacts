package com.example.lessons

import android.content.Context
import android.provider.ContactsContract
import java.sql.Date
import java.util.GregorianCalendar

class ContactProvider {

    fun getShortContactsDetails(context: Context): ArrayList<Contact> {
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
                        var numbers: Array<String?>? = null
                        val hasPhoneNumber =
                            cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                                .toInt()
                        if (hasPhoneNumber > 0) {
                            numbers = getNumbers(id, context)
                        }
                        val contact =
                            Contact(
                                name = name,
                                number1 = numbers?.get(0),
                                number2 = null,
                                email1 = null,
                                email2 = null,
                                description = null,
                                birthday = null,
                                id = id
                            )
                        contacts.add(contact)
                    }
                }
            }
        }
        return contacts
    }

    fun getFullContactDetails(id: String, context: Context): Contact {
        val contentUri = ContactsContract.Contacts.CONTENT_URI
        val idColumn = ContactsContract.Contacts._ID
        val displayName = ContactsContract.Contacts.DISPLAY_NAME
        var contact = Contact(
            name = null,
            number1 = null,
            number2 = null,
            email1 = null,
            email2 = null,
            description = null,
            birthday = null,
            id = id
        )
        val cursor = context.contentResolver.query(
            contentUri, null,
            "$idColumn = $id", null, null
        )

        cursor.use {
            if (cursor != null) {
                cursor.moveToNext()
                val name = cursor.getString(cursor.getColumnIndexOrThrow(displayName))
                val numbers = getNumbers(id, context)
                val email = getEmail(id, context)
                val birthday = getBirthday(id, context)
                contact = Contact(
                    name = name,
                    number1 = numbers[0],
                    number2 = numbers[1],
                    email1 = email[0],
                    email2 = email[1],
                    description = null,
                    birthday = birthday,
                    id = id
                )

            }
        }
        return contact
    }

    private fun getNumbers(contact_id: String, context: Context): Array<String?> {
        val phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneContactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val numberColumn = ContactsContract.CommonDataKinds.Phone.NUMBER
        val numbers = arrayOfNulls<String>(2)
        var count = 0
        val phoneCursor = context.contentResolver.query(
            phoneContentUri, null,
            "$phoneContactId = ?", arrayOf(contact_id), null
        )

        phoneCursor.use {
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    val phoneNumber =
                        StringBuilder(phoneCursor.getString(phoneCursor.getColumnIndexOrThrow(numberColumn)))
                    if (phoneNumber[0] == '8') {
                        phoneNumber.replace(0, 1, "+7")
                    }
                    val number = phoneNumber.toString()
                        .replace(" ", "")
                        .replace("-", "")
                    if (!numbers.contains(number)) {
                        numbers[count] = number
                        count++
                    }
                }
            }
        }
        return numbers
    }

    private fun getEmail(contact_id: String, context: Context): Array<String?> {
        val number = ContactsContract.CommonDataKinds.Phone.NUMBER
        val emailContentUri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        val emailContactId = ContactsContract.CommonDataKinds.Email.CONTACT_ID
        val emails = arrayOfNulls<String>(2)
        var count = 0
        val emailCursor = context.contentResolver.query(
            emailContentUri, null,
            "$emailContactId = ?", arrayOf(contact_id), null
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

    private fun getBirthday(contact_id: String, context: Context): GregorianCalendar? {
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
                ContactsContract.Data.CONTACT_ID + " = " + contact_id
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
}