package com.example.lessons

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import java.text.DecimalFormat
import java.time.Duration
import java.util.*
import java.util.Calendar.YEAR
import java.util.concurrent.TimeUnit


class DetailsFragment : GetDetails, Fragment(R.layout.fragment_details) {

    private var contactId: Int = -1
    private val handler = Handler(Looper.getMainLooper())
    private var name: TextView? = null
    private var number1: TextView? = null
    private var number2: TextView? = null
    private var email1: TextView? = null
    private var email2: TextView? = null
    private var description: TextView? = null
    private var birthday: TextView? = null
    private var birthdaySwitch: SwitchCompat? = null
    private lateinit var contactBirthday: GregorianCalendar
    private lateinit var intentBirthdayReceiver: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    companion object {
        private const val ARG: String = "arg"
        fun newInstance(id: Int) = DetailsFragment().apply {
            arguments = bundleOf(
                ARG to id
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = requireView().findViewById(R.id.nameTextView)
        number1 = requireView().findViewById(R.id.number1TextView)
        number2 = requireView().findViewById(R.id.number2TextView)
        email1 = requireView().findViewById(R.id.eMail1TextView)
        email2 = requireView().findViewById(R.id.eMail2TextView)
        description = requireView().findViewById(R.id.descriptionTextView)
        birthday = requireView().findViewById(R.id.birthdayTextView)
        birthdaySwitch = requireView().findViewById(R.id.birthdaySwitch)
        val args = requireArguments()
        contactId = args.getInt(ARG)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_details)
        mainActivity.contactService.getDetailsById(this, contactId)

        birthdaySwitch?.isClickable = false
        intentBirthdayReceiver = Intent("birthdayReceiver").apply {
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context?.let { intentBirthdayReceiver.setClass(it, BirthdayReceiver::class.java) }
        alarmManager = context?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        if (PendingIntent.getBroadcast(
                context,
                contactId,
                intentBirthdayReceiver,
                PendingIntent.FLAG_NO_CREATE
            ) != null) {
            birthdaySwitch?.isChecked = true
        }
        birthdaySwitch?.setOnCheckedChangeListener() { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(context, "You will be reminded about birthday", Toast.LENGTH_LONG)
                    .show()
                doAlarm()
            } else {
                Toast.makeText(context, "Birthday notification is off", Toast.LENGTH_LONG).show()
                pendingIntent = PendingIntent.getBroadcast(
                    context,
                    contactId,
                    intentBirthdayReceiver,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                pendingIntent.cancel()
                alarmManager.cancel(pendingIntent)
            }
        }
    }

    override fun getDetails(contactForDetails: Contact) {
        contactBirthday = contactForDetails.birthday
        handler.post {
            name?.text = contactForDetails.name
            number1?.text = contactForDetails.number1
            number2?.text = contactForDetails.number2
            email1?.text = contactForDetails.email1
            email2?.text = contactForDetails.email2
            description?.text = contactForDetails.description
            val data = StringJoiner(".")
            val format = DecimalFormat("00")
            data.add(format.format(contactBirthday.get(Calendar.DAY_OF_MONTH)))
                .add(format.format(contactBirthday.get(Calendar.MONTH) + 1))
                .add(format.format(contactBirthday.get(YEAR)))
            birthday?.text = data.toString()
            birthdaySwitch?.isClickable = true
            intentBirthdayReceiver.putExtra(
                "nameOfContact",
                "Today is " + name?.text + "'s birthday"
            )
            intentBirthdayReceiver.putExtra("contactId", contactId)
        }
    }

    private fun doAlarm() {
        pendingIntent = PendingIntent.getBroadcast(
            context,
            contactId,
            intentBirthdayReceiver,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        if (calendar[Calendar.DAY_OF_YEAR] > contactBirthday.get(Calendar.DAY_OF_YEAR)) {
            calendar.add(YEAR, 1)
        }
        if (contactBirthday.get(Calendar.MONTH) == Calendar.FEBRUARY && contactBirthday.get(Calendar.DAY_OF_MONTH) == 29) {
            contactBirthday.set(Calendar.DAY_OF_MONTH, 28)
        }
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.DAY_OF_MONTH] = contactBirthday.get(Calendar.DAY_OF_MONTH)
        calendar[Calendar.MONTH] = contactBirthday.get(Calendar.MONTH)
            alarmManager.set(
                AlarmManager.RTC,
                calendar.timeInMillis,
                pendingIntent
            )

    }

    override fun onDestroyView() {
        name = null
        number1 = null
        number2 = null
        email1 = null
        email2 = null
        description = null
        birthday = null
        birthdaySwitch = null
        super.onDestroyView()
    }
}