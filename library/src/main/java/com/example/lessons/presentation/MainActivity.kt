package com.example.lessons.presentation

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
import com.example.lessons.contactDetails.presentation.ContactDetailsFragment
import com.example.lessons.contactList.presentation.ContactListFragment
import com.example.lessons.themePicker.presentation.ThemeDelegate
import com.example.lessons.utils.constans.BIRTHDAY_CONTACT_ID_INTENT_KEY
import com.example.lessons.utils.constans.NOTIFICATION_CHANNEL_ID
import com.example.lessons.utils.constans.NOTIFICATION_CHANNEL_NAME
import com.example.library.R


internal class MainActivity : AppCompatActivity() {

    private val requestCodeReadContacts = 1
    private var readContactsGranted = false
    val themeDelegate: ThemeDelegate by lazy(LazyThreadSafetyMode.NONE) {
        ThemeDelegate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        checkReadContactPermission()
        themeDelegate.setTheme()

        if (savedInstanceState == null && readContactsGranted) {
            navigateToListFragment()
        }
        if (intent.getIntExtra(
                BIRTHDAY_CONTACT_ID_INTENT_KEY,
                -1
            ) != -1 && savedInstanceState == null
        ) {
            navigateToBirthdayContact()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                importance
            )
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
            navigateToListFragment()
        } else {
            Toast.makeText(
                this,
                getString(R.string.require_permission_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkReadContactPermission() {
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
    }

    private fun navigateToListFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, ContactListFragment())
            .commit()
    }

    private fun navigateToBirthdayContact() {
        supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ContactDetailsFragment.newInstance(
                    intent.getIntExtra(
                        BIRTHDAY_CONTACT_ID_INTENT_KEY,
                        -1
                    )
                )
            )
            .addToBackStack(BIRTHDAY_CONTACT_DETAILS_FRAGMENT_BACK_STACK_KEY)
            .commit()
    }

    private companion object {
        val BIRTHDAY_CONTACT_DETAILS_FRAGMENT_BACK_STACK_KEY: String =
            ContactDetailsFragment::class.java.simpleName
    }
}