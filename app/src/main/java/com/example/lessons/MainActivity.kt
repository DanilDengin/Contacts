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
import com.example.contact.api.ContactsScreenApi
import com.example.di.dependency.FeatureExternalDepsContainer
import com.example.di.dependency.FeatureExternalDepsProvider
import com.example.themePicker.impl.presentation.ThemeDelegate
import com.example.ui.R
import com.example.utils.constans.BIRTHDAY_CONTACT_DEFAULT_ID
import com.example.utils.constans.BIRTHDAY_CONTACT_ID_INTENT_KEY
import com.example.utils.constans.NOTIFICATION_CHANNEL_ID
import com.example.utils.delegate.unsafeLazy
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

internal class MainActivity : AppCompatActivity(R.layout.activity_main), FeatureExternalDepsProvider {

    @Inject
    override lateinit var deps: FeatureExternalDepsContainer
        internal set

    @Inject
    lateinit var themeDelegate: ThemeDelegate

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router

    private val navigator: Navigator = AppNavigator(this, R.id.fragmentContainer)

    @Inject
    lateinit var contactsScreenApi: ContactsScreenApi

    private val requestCodeReadContacts = 1

    private var readContactsGranted = false

    private val contactId: Int by unsafeLazy {
        intent.getIntExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, BIRTHDAY_CONTACT_DEFAULT_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        createNotificationChannel()
        checkReadContactPermission()
        themeDelegate.setTheme()

        if (savedInstanceState == null && readContactsGranted) {
            navigateToListFragment()
        }
        if (contactId != -1 && savedInstanceState == null && readContactsGranted) {
            navigateToBirthdayContact()
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
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
        router.newRootScreen(contactsScreenApi.getListScreen())
    }

    private fun navigateToBirthdayContact() {
        router.navigateTo(contactsScreenApi.getDetailsScreen(contactId))
    }

    private companion object {
        const val NOTIFICATION_CHANNEL_NAME = "birthdayReminders"
    }
}