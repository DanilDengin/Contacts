package com.example.lessons

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.provider.ContactsContract
import java.sql.Date
import java.util.GregorianCalendar


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
            val contactProvider =ContactProvider()
            val contacts = contactProvider.getShortContactsDetails(context)
            Thread.sleep(1000)
            getContactList.getContactList(contacts)
        }.start()
    }

    fun getDetailsById(getDetails: GetDetails, id: String, context: Context) {
        Thread {
            val contactProvider =ContactProvider()
            val contact = contactProvider.getFullContactDetails(id, context)
            Thread.sleep(1000)
            getDetails.getDetails(contact)
        }.start()
    }

}




