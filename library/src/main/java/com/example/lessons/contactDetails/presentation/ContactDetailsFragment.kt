package com.example.lessons.contactDetails.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactDetails.di.DaggerContactDetailsComponent
import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.di.ContactComponentDependenciesProvider
import com.example.lessons.presentation.MainActivity
import com.example.lessons.utils.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentDetailsBinding
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Calendar.YEAR
import java.util.GregorianCalendar
import java.util.StringJoiner
import javax.inject.Inject
import javax.inject.Provider


internal class ContactDetailsFragment : GetDetails, Fragment(R.layout.fragment_details) {

    private val binding by viewBinding(FragmentDetailsBinding::bind)
    private var contactId: Int = -1
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

    @Inject
    lateinit var viewModelFactoryProvider: Provider<ContactDetailsViewModel.Factory>

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        viewModel {
            viewModelFactoryProvider.get().create(contactId.toString())
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerContactDetailsComponent.builder()
            .contactComponentDependencies(
                (requireContext().applicationContext as ContactComponentDependenciesProvider)
                    .getContactComponentDependencies()
            )
            .build()
            .also { it.inject(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = requireArguments()
        contactId = args.getInt(ARG)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_details)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.user.observe(viewLifecycleOwner, ::getDetails)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.exceptionState.observe(viewLifecycleOwner) { showExceptionToast() }

        intentBirthday.setClass(requireContext(), BirthdayReceiver::class.java)
        if (PendingIntent.getBroadcast(
                context,
                contactId,
                intentBirthday,
                PendingIntent.FLAG_NO_CREATE
            ) != null
        ) {
            binding.birthdaySwitch.isChecked = true
        }
        binding.birthdaySwitch.setOnCheckedChangeListener { _, isChecked ->
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
        with(binding) {
            nameTextView.text = contactForDetails.name
            number1TextView.text = contactForDetails.number1
            number2TextView.text = contactForDetails.number2
            eMail1TextView.text = contactForDetails.email1
            eMail2TextView.text = contactForDetails.email2
            number2TextView.isGone = contactForDetails.number2 == null
            number2ImageView.isGone = contactForDetails.number2 == null
            eMail1TextView.isGone = contactForDetails.email1 == null
            email1ImageView.isGone = contactForDetails.email1 == null
            eMail2TextView.isGone = contactForDetails.email2 == null
            email2ImageView.isGone = contactForDetails.email2 == null
            descriptionTextView.text = contactForDetails.description
        }
        birthdayDate = contactForDetails.birthday
        if (birthdayDate != null) {
            val data = StringJoiner(".")
            val format = DecimalFormat("00")
            data.add(format.format(birthdayDate?.get(Calendar.DAY_OF_MONTH)))
                .add(format.format(requireNotNull(birthdayDate).get(Calendar.MONTH) + 1))
                .add(format.format(birthdayDate?.get(YEAR)))
            binding.birthdayTextView.text = data.toString()
            binding.birthdaySwitch.isClickable = true
            intentBirthday
                .putExtra(
                    BIRTHDAY_CONTACT_NAME_INTENT_KEY,
                    activity?.getString(R.string.notification_text) + binding.nameTextView.text
                )
                .putExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, contactId)
        }
    }

    private fun doAlarm() {
        alarmBirthday.set(
            AlarmManager.RTC,
            viewModel.getAlarmDate(),
            pendingIntentBirthday
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            parentFragmentManager.popBackStack()
        }
        return true
    }

    private fun setLoadingIndicator(isVisible: Boolean) {
        binding.progressBarDetails.isVisible = isVisible
        binding.detailsFragmentContainer.isVisible = isVisible.not()
    }

    private fun showExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.toast_exception),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val BIRTHDAY_CONTACT_NAME_INTENT_KEY = "nameOfContact"
        private const val BIRTHDAY_CONTACT_ID_INTENT_KEY = "contactId"
        private const val ARG: String = "arg"
        fun newInstance(id: Int) = ContactDetailsFragment().apply {
            arguments = bundleOf(
                ARG to id
            )
        }
    }
}