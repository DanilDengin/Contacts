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
        var contact: Contact
        val cursor = context.contentResolver.query(
            contentUri, null,
            null, null, null
        )

        cursor.use {
            if (it != null) {
                if (it.count > 0) {
                    while (it.moveToNext()) {
                        val id: String = it.getString(it.getColumnIndexOrThrow(idColumn))
                        val name = it.getString(it.getColumnIndexOrThrow(displayName))
                        var phones: Array<String?>? = null
                        val hasPhoneNumber =
                            it.getString(it.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                                .toInt()
                        if (hasPhoneNumber > 0) {
                            phones = getPhones(id, context)
                        }
                        contact = Contact(name, phones?.get(0), null, null, null, null, null, id)
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
        var contact = Contact(null, null, null, null, null, null, null, id)
        val cursor = context.contentResolver.query(
            contentUri, null,
            "$idColumn = $id", null, null
        )

        cursor.use {
            if (it != null) {
                it.moveToNext()
                val name = it.getString(it.getColumnIndexOrThrow(displayName))
                val phone = getPhones(id, context)
                val email = getEmail(id, context)
                val birthday = getBirthday(id, context)
                contact = Contact(name, phone[0], phone[1], email[0], email[1], null, birthday, id)
            }
        }
        return contact
    }

    private fun getPhones(contact_id: String, context: Context): Array<String?> {
        val phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val phoneContactId = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val number = ContactsContract.CommonDataKinds.Phone.NUMBER
        val phones = arrayOfNulls<String>(2)
        var count = 0
        val phoneCursor = context.contentResolver.query(
            phoneContentUri, null,
            "$phoneContactId = ?", arrayOf(contact_id), null
        )

        phoneCursor.use {
            if (it != null) {
                while (it.moveToNext()) {
                    val phoneNumber =
                        StringBuilder(it.getString(it.getColumnIndexOrThrow(number)))
                    if (phoneNumber[0] == '8') {
                        phoneNumber.replace(0, 1, "+7")
                    }
                    val phone = phoneNumber.toString()
                        .replace(" ", "")
                        .replace("-", "")
                    if (!phones.contains(phone)) {
                        phones[count] = phone
                        count++
                    }
                }
            }
        }
        return phones
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
            if (it != null) {
                while (it.moveToNext()) {
                    emails[count] = (it.getString(it.getColumnIndexOrThrow(number)))
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
            if (it != null) {
                while (it.moveToNext())
                    birthday =
                        it.getString(it.getColumnIndexOrThrow(startDate))
            }
        }
        if (birthday != null) {
            birthdayDate = GregorianCalendar()
            birthdayDate.time = Date.valueOf(birthday)
        }
        return birthdayDate
    }
}