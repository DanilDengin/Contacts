package com.example.lessons


import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lessons.contactdetails.DetailsFragment
import com.example.lessons.contactslist.ContactsListFragment


class MainActivity : AppCompatActivity() {

    private val requestCodeReadContacts = 1
    private var readContactsGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        val hasReadContactPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
        if (hasReadContactPermission == PackageManager.PERMISSION_GRANTED) {
            readContactsGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                requestCodeReadContacts
            )
        }

        if (savedInstanceState == null && readContactsGranted) {
            showListFragment()
        }

        if (intent.getIntExtra("contactId", -1) != -1 && savedInstanceState == null) {
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeReadContacts) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readContactsGranted = true
            }
        }
        if (readContactsGranted) {
            showListFragment()
        } else {
            Toast.makeText(
                this,
                "Without permission the app can't function properly",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun showListFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
            .add(R.id.fragmentContainer, ContactsListFragment())
            .commit()
    }
}