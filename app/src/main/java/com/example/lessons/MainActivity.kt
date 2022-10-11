package com.example.lessons


import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var contactService: ContactService
    private var bound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ContactService.ContactBinder
            contactService = binder.getService()
            bound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction
                .add(R.id.fragmentContainer, ListFragment())
                .commit()
        }

        val intentService = Intent(this, ContactService::class.java)
        bindService(intentService, connection, Context.BIND_AUTO_CREATE)
        contactService = ContactService()


        if (intent.getIntExtra("contactId", -1) != -1) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction
                .replace(
                    R.id.fragmentContainer,
                    DetailsFragment.newInstance(intent.getIntExtra("contactId", -1))
                )
                .addToBackStack("toBirthdayDetails")
                .commit()
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Birthday", name, importance)
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onDestroy() {
        if (bound) {
            unbindService(connection)
            bound = false
            Toast.makeText(this, "Service is destoyed", Toast.LENGTH_LONG).show()
        }
        super.onDestroy()
    }
}


