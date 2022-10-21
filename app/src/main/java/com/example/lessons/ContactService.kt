package com.example.lessons

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder


class ContactService : Service() {

    private val binder = ContactBinder()
    private val contactProvider = ContactProvider()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(getContactList: GetContactList, context: Context) {
        Thread {
            val contacts = contactProvider.getShortContactsDetails(context)
            Thread.sleep(1000)
            getContactList.getContactList(contacts)
        }.start()
    }

    fun getDetailsById(getDetails: GetDetails, id: String, context: Context) {
        Thread {
            val contact = contactProvider.getFullContactDetails(id, context)
            Thread.sleep(1000)
            getDetails.getDetails(contact)
        }.start()
    }

}




