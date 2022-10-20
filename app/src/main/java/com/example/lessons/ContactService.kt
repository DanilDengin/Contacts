package com.example.lessons

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract


class ContactService : Service() {

    private val binder = ContactBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(getContactList: GetContactList, context: Context) {
        Thread {
            val contacts = getShortContactsDetails(context)
            Thread.sleep(1000)
            getContactList.getContactList(contacts)
        }.start()
    }

    fun getDetailsById(getDetails: GetDetails, id: String, context: Context) {
        Thread {
            val contact = getFullContactDetails(id, context)
            Thread.sleep(1000)
            getDetails.getDetails(contact)
        }.start()
    }

    @SuppressLint("Range")
    private fun getShortContactsDetails(context: Context): ArrayList<Contact> {
        val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI
        val _ID = ContactsContract.Contacts._ID
        val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
        val HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER
        val contacts = ArrayList<Contact>()
        var contact: Contact
        val cursor = context.contentResolver.query(
            CONTENT_URI, null,
            null, null, null
        )

        cursor.use { cursor ->
            if (cursor != null) {
                if (cursor.count > 0) {
                    while (cursor.moveToNext()) {
                        val id: String = cursor.getString(cursor.getColumnIndex(_ID))
                        val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                        var phones: Array<String?>? = null
                        val hasPhoneNumber =
                            cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)).toInt()
                        if (hasPhoneNumber > 0) {
                            phones = getPhones(id, context)
                        }
                        contact = Contact(name, phones?.get(0), id)
                        contacts.add(contact)
                    }
                }
            }
        }
        return contacts
    }

    @SuppressLint("Range")
    private fun getFullContactDetails(id: String, context: Context): Contact {
        val CONTENT_URI = ContactsContract.Contacts.CONTENT_URI
        val _ID = ContactsContract.Contacts._ID
        val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
        var contact = Contact(null, null, id)
        val cursor = context.contentResolver.query(
            CONTENT_URI, null,
            _ID + " = " + id, null, null
        )

        cursor.use { cursor ->
            if (cursor != null) {
                cursor.moveToNext()
                val name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME))
                val phone = getPhones(id, context)
                val email = getEmail(id, context)
                val birthday = getBirthday(id, context)
                contact = Contact(name, phone[0], phone[1], email[0], email[1], null, birthday, id)
            }
        }
        return contact
    }


    @SuppressLint("Range")
    private fun getPhones(contact_id: String, context: Context): Array<String?> {
        val PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
        val phones = arrayOfNulls<String>(2)
        var count = 0
        val phoneCursor = context.contentResolver.query(
            PhoneCONTENT_URI, null,
            "$Phone_CONTACT_ID = ?", arrayOf(contact_id), null
        )

        phoneCursor.use { phoneCursor ->
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {
                    val PHONE_NUMBER =
                        StringBuilder(phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)))
                    if (PHONE_NUMBER[0] == '8') {
                        PHONE_NUMBER.replace(0, 1, "+7")
                    }
                    val phone = PHONE_NUMBER.toString()
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

    @SuppressLint("Range")
    private fun getEmail(contact_id: String, context: Context): Array<String?> {
        val NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER
        val EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        val Email_CONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID
        val email = arrayOfNulls<String>(2)
        var count = 0
        val emailCursor = context.contentResolver.query(
            EmailCONTENT_URI, null,
            "$Email_CONTACT_ID = ?", arrayOf(contact_id), null
        )

        emailCursor.use { emailCursor ->
            if (emailCursor != null) {
                while (emailCursor.moveToNext()) {
                    email[count] = (emailCursor.getString(emailCursor.getColumnIndex(NUMBER)))
                    count++
                }
            }
        }
        return email
    }

    @SuppressLint("Range")
    private fun getBirthday(contact_id: String, context: Context): String? {
        val BirthdayCONTENT_URI = ContactsContract.Data.CONTENT_URI
        val DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME
        val START_DATE = ContactsContract.CommonDataKinds.Event.START_DATE
        var birthday: String? = null
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
            BirthdayCONTENT_URI, columns,
            where, null, DISPLAY_NAME
        )

        birthdayCursor.use { birthdayCursor ->
            if (birthdayCursor != null) {
                while (birthdayCursor.moveToNext()) {
                    birthday =
                        birthdayCursor.getString(birthdayCursor.getColumnIndex(START_DATE))
                }
            }
        }
        return birthday
    }
}




