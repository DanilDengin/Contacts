package com.example.lessons

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


class ContactService : Service() {
    val Danil = Contact(
        "Данил",
        "89052550588",
        "8936906342",
        "dd@mail.ru",
        "fwef@mail.ru",
        "description about Danil"
    )
    val Ilnaz = Contact(
        "Ильназ",
        "8888888888",
        "8932141242",
        "ik@mail.ru",
        "fwef@mail.ru",
        "description about Ilnaz"
    )
    val contacts = arrayOf(Danil, Ilnaz)
    private val binder = ContactBinder()

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(applicationContext, "Service is active", Toast.LENGTH_SHORT).show()
        return binder
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(func: GetInformation) {
        Thread {
            Thread.sleep(3000)
            func.getList(contacts[0], contacts[1])
        }.start()

    }

    fun getDetailsByID(func: GetInformation, ID: Int) {
        Thread {
            Thread.sleep(3000)
            func.getDetails(contacts[ID])
        }.start()
    }

}

interface GetInformation {
    fun getList(contact1: Contact, contact2: Contact)
    fun getDetails(contForDetails: Contact)
}



