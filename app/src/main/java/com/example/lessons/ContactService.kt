package com.example.lessons

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import java.util.*


class ContactService : Service() {
    private val danil = Contact(
        "Danil",
        "89052550588",
        "8936906342",
        "dd@mail.ru",
        "fwef@mail.ru",
        "description about Danil",
        GregorianCalendar(2022, Calendar.OCTOBER, 12)
    )
    private val ilnaz = Contact(
        "Ilnaz",
        "8888888888",
        "8932141242",
        "ik@mail.ru",
        "fwef@mail.ru",
        "description about Ilnaz",
        GregorianCalendar(1999, Calendar.SEPTEMBER, 23)
    )
    private val contacts = arrayOf(danil, ilnaz)
    private val binder = ContactBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(getContactList: GetContactList) {
        Thread {
            Thread.sleep(1000)
            getContactList.getContactList(contacts)
        }.start()

    }

    fun getDetailsById(getDetails: GetDetails, id: Int) {
        Thread {
            Thread.sleep(1000)
            getDetails.getDetails(contacts[id])
        }.start()
    }

}




