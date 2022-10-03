package com.example.lessons

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.widget.Toast


class ContactService : Service() {
    private val danil = Contact(
        "Данил",
        "89052550588",
        "8936906342",
        "dd@mail.ru",
        "fwef@mail.ru",
        "description about Danil"
    )
    private val ilnaz = Contact(
        "Ильназ",
        "8888888888",
        "8932141242",
        "ik@mail.ru",
        "fwef@mail.ru",
        "description about Ilnaz"
    )
    private val contacts = arrayOf(danil, ilnaz)
    private val binder = ContactBinder()

    override fun onBind(intent: Intent): IBinder {
        Toast.makeText(applicationContext, "Service is active", Toast.LENGTH_SHORT).show()
        return binder
    }

    inner class ContactBinder : Binder() {
        fun getService(): ContactService = this@ContactService
    }

    fun getContacts(getContactList: GetContactList) {
        Thread {
            Thread.sleep(3000)
            getContactList.getContactList(contacts)
        }.start()

    }

    fun getDetailsById(getDetailsById: GetDetailsById, id: Int) {
        Thread {
            Thread.sleep(3000)
            getDetailsById.getDetailsById(contacts[id])
        }.start()
    }

}




