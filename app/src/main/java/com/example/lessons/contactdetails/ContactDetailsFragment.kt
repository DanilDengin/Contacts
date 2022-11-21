package com.example.lessons.contactdetails

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lessons.Contact
import com.example.lessons.MainActivity
import com.example.lessons.R
import com.example.lessons.receivers.BirthdayReceiver
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Calendar.YEAR
import java.util.GregorianCalendar
import java.util.StringJoiner


class DetailsFragment : GetDetails, Fragment(R.layout.fragment_details) {

    private var contactId: Int = -1
    private var progressBar: ProgressBar? = null
    private var container: ConstraintLayout? = null
    private var name: TextView? = null
    private var number1: TextView? = null
    private var number1Image: ImageView? = null
    private var number2: TextView? = null
    private var number2Image: ImageView? = null
    private var email1: TextView? = null
    private var email1Image: ImageView? = null
    private var email2: TextView? = null
    private var email2Image: ImageView? = null
    private var description: TextView? = null
    private var birthday: TextView? = null
    private var birthdaySwitch: SwitchCompat? = null
    private var birthdayDate: GregorianCalendar? = null
    private val intentBirthday: Intent by lazy(LazyThreadSafetyMode.NONE) { Intent("birthdayReceiver") }
    private val pendingIntentBirthday: PendingIntent by lazy(LazyThreadSafetyMode.NONE) {
        PendingIntent.getBroadcast(
            context,
            contactId,
            intentBirthday,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
    private val alarmBirthday: AlarmManager by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(
            AppCompatActivity.ALARM_SERVICE
        ) as AlarmManager
    }
    private val viewModel: ContactDetailsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ContactDetailsViewModelFactory(
                requireArguments().getInt(ARG).toString(),
                requireActivity().applicationContext
            )
        )[ContactDetailsViewModel::class.java]
    }

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
        progressBar = requireView().findViewById(R.id.progressBarDetails)
        container = requireView().findViewById(R.id.detailsFragment)
        name = requireView().findViewById(R.id.nameTextView)
        number1 = requireView().findViewById(R.id.number1TextView)
        number1Image = requireView().findViewById(R.id.number1Image)
        number2 = requireView().findViewById(R.id.number2TextView)
        number2Image = requireView().findViewById(R.id.number2Image)
        email1 = requireView().findViewById(R.id.eMail1TextView)
        email1Image = requireView().findViewById(R.id.email1Image)
        email2 = requireView().findViewById(R.id.eMail2TextView)
        email2Image = requireView().findViewById(R.id.email2Image)
        description = requireView().findViewById(R.id.descriptionTextView)
        birthday = requireView().findViewById(R.id.birthdayTextView)
        birthdaySwitch = requireView().findViewById(R.id.birthdaySwitch)
        val args = requireArguments()
        contactId = args.getInt(ARG)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_details)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.getUserDetails().observe(viewLifecycleOwner, ::getDetails)
        viewModel.getProgressBarState().observe(viewLifecycleOwner, ::setLoadingIndicator)

        birthdaySwitch?.isClickable = false
        intentBirthday.setClass(requireContext(), BirthdayReceiver::class.java)
        if (PendingIntent.getBroadcast(
                context,
                contactId,
                intentBirthday,
                PendingIntent.FLAG_NO_CREATE
            ) != null
        ) {
            birthdaySwitch?.isChecked = true
        }
        birthdaySwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    context,
                    getString(R.string.toast_remind_birthday),
                    Toast.LENGTH_LONG
                )
                    .show()
                doAlarm()
            } else {
                Toast.makeText(context, getString(R.string.toast_cancel_remind), Toast.LENGTH_LONG)
                    .show()
                pendingIntentBirthday.cancel()
                alarmBirthday.cancel(pendingIntentBirthday)
            }
        }
    }

    override fun getDetails(contactForDetails: Contact) {
        birthdayDate = contactForDetails.birthday
        name?.text = contactForDetails.name
        number1?.text = contactForDetails.number1
        number2?.text = contactForDetails.number2
        email1?.text = contactForDetails.email1
        email2?.text = contactForDetails.email2
        number2?.isGone = contactForDetails.number2 == null
        number2Image?.isGone = contactForDetails.number2 == null
        email1?.isGone = contactForDetails.email1 == null
        email1Image?.isGone = contactForDetails.email1 == null
        email2?.isGone = contactForDetails.email2 == null
        email2Image?.isGone = contactForDetails.email2 == null
        description?.text = contactForDetails.description
        if (birthdayDate != null) {
            val data = StringJoiner(".")
            val format = DecimalFormat("00")
            data.add(format.format(birthdayDate?.get(Calendar.DAY_OF_MONTH)))
                .add(format.format(requireNotNull(birthdayDate).get(Calendar.MONTH) + 1))
                .add(format.format(birthdayDate?.get(YEAR)))
            birthday?.text = data.toString()
            birthdaySwitch?.isClickable = true
            intentBirthday
                .putExtra(
                    "nameOfContact",
                    activity?.getString(R.string.notification_text) + name?.text
                )
                .putExtra("contactId", contactId)
        }
    }

    private fun doAlarm() {
        val birthdayDateNotNull = requireNotNull(birthdayDate)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        if (calendar[Calendar.DAY_OF_YEAR] > birthdayDateNotNull.get(Calendar.DAY_OF_YEAR)) {
            calendar.add(YEAR, 1)
        }
        if (birthdayDateNotNull.get(Calendar.MONTH) == Calendar.FEBRUARY && birthdayDateNotNull.get(
                Calendar.DAY_OF_MONTH
            ) == 29
        ) {
            birthdayDateNotNull.set(Calendar.DAY_OF_MONTH, 28)
        }
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.DAY_OF_MONTH] = birthdayDateNotNull.get(Calendar.DAY_OF_MONTH)
        calendar[Calendar.MONTH] = birthdayDateNotNull.get(Calendar.MONTH)
        alarmBirthday.set(
            AlarmManager.RTC,
            calendar.timeInMillis,
            pendingIntentBirthday
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            parentFragmentManager.popBackStack()
        }
        return true
    }

    private fun setLoadingIndicator(state: Boolean) {
        progressBar?.isGone = state
        container?.isGone = state.not()
    }

    override fun onDestroyView() {
        progressBar = null
        container = null
        name = null
        number1 = null
        number1Image = null
        number2 = null
        number2Image = null
        email1 = null
        email1Image = null
        email2 = null
        email2Image = null
        description = null
        birthday = null
        birthdaySwitch = null
        super.onDestroyView()
    }
}