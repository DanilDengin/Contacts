package com.example.contact.impl.presentation.details

import com.example.contact.impl.R as FutureRes
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contact.api.entity.Contact
import com.example.contact.impl.databinding.FragmentDetailsBinding
import com.example.contact.impl.domain.receiver.BirthdayReceiverProvider
import com.example.contact.impl.presentation.ContactComponentViewModel
import com.example.contact.impl.presentation.ContactsComponentDependenciesProvider
import com.example.di.dependency.findFeatureExternalDeps
import com.example.mvvm.getComponentViewModel
import com.example.mvvm.viewModel
import com.example.ui.R
import com.example.utils.constans.BIRTHDAY_CONTACT_ID_INTENT_KEY
import com.example.utils.constans.BIRTHDAY_CONTACT_NAME_INTENT_KEY
import com.example.utils.constans.BIRTHDAY_RECEIVER_INTENT_ACTION
import com.example.utils.delegate.unsafeLazy
import com.example.utils.idlingResource.TestIdlingResource
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Calendar.YEAR
import java.util.GregorianCalendar
import java.util.StringJoiner
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch

internal class ContactDetailsFragment : Fragment(FutureRes.layout.fragment_details) {

    @VisibleForTesting
    val idlingResource = TestIdlingResource()

    @Inject
    lateinit var viewModelFactoryProvider: Provider<ContactDetailsViewModel.Factory>

    @Inject
    lateinit var birthdayReceiverProvider: BirthdayReceiverProvider

    private val binding by viewBinding(FragmentDetailsBinding::bind)

    private var contactId: Int = -1

    private var birthdayDate: GregorianCalendar? = null

    private val intentBirthday: Intent by unsafeLazy {
        Intent(BIRTHDAY_RECEIVER_INTENT_ACTION)
    }

    private val pendingIntentBirthday: PendingIntent by unsafeLazy {
        PendingIntent.getBroadcast(
            context,
            contactId,
            intentBirthday,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val alarmBirthday: AlarmManager by unsafeLazy {
        requireContext().getSystemService(
            AppCompatActivity.ALARM_SERVICE
        ) as AlarmManager
    }

    private val birthdayNotificationText: String by unsafeLazy {
        resources.getString(R.string.birthday_notification_text)
    }

    private val viewModel by unsafeLazy {
        viewModel {
            viewModelFactoryProvider.get().create(contactId.toString())
        }
    }

    override fun onAttach(context: Context) {
        ContactsComponentDependenciesProvider.contactsExternalDependencies = findFeatureExternalDeps()
        getComponentViewModel<ContactComponentViewModel>().contactsComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.contact_details_toolbar)
        contactId = requireArguments().getInt(ARG)
        viewModel.user.observe(viewLifecycleOwner, ::updateView)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.exceptionState.observe(viewLifecycleOwner) { showExceptionToast() }

        intentBirthday.setClass(
            requireContext(),
            birthdayReceiverProvider.getReceiver()::class.java
        )
        if (PendingIntent.getBroadcast(
                context,
                contactId,
                intentBirthday,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            ) != null
        ) {
            binding.birthdaySwitch.isChecked = true
        }
        binding.birthdaySwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    context,
                    getString(R.string.remind_birthday_toast),
                    Toast.LENGTH_LONG
                ).show()
                doAlarm()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.cancel_birthday_remind_toast),
                    Toast.LENGTH_LONG
                )
                    .show()
                pendingIntentBirthday.cancel()
                alarmBirthday.cancel(pendingIntentBirthday)
            }
        }

        binding.mapButton.setOnClickListener {
            navigateToContactMapFragment()
        }
    }

    private fun navigateToContactMapFragment() {
        viewModel.navigateToMapFragment()
    }

    private fun updateView(contactForDetails: Contact) {
        with(binding) {
            nameTextView.text = contactForDetails.name
            number1TextView.text = contactForDetails.numberPrimary
            number2TextView.text = contactForDetails.numberSecondary
            eMail1TextView.text = contactForDetails.emailPrimary
            eMail2TextView.text = contactForDetails.emailSecondary
            descriptionTextView.text = contactForDetails.description
            number2TextView.isGone = contactForDetails.numberSecondary == null
            number2ImageView.isGone = contactForDetails.numberSecondary == null
            eMail1TextView.isGone = contactForDetails.emailPrimary == null
            email1ImageView.isGone = contactForDetails.emailPrimary == null
            eMail2TextView.isGone = contactForDetails.emailSecondary == null
            email2ImageView.isGone = contactForDetails.emailSecondary == null
            descriptionTextView.isGone = contactForDetails.description == null
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
                    String.format(birthdayNotificationText, " ${binding.nameTextView.text}")
                )
                .putExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, contactId)
        }
    }

    private fun doAlarm() {
        viewLifecycleOwner.lifecycleScope.launch {
            alarmBirthday.set(
                AlarmManager.RTC,
                viewModel.getAlarmDate(),
                pendingIntentBirthday
            )
        }
    }

    private fun setLoadingIndicator(isVisible: Boolean) {
        binding.progressBarDetails.isVisible = isVisible
        binding.detailsFragmentContainer.isVisible = isVisible.not()
    }

    private fun showExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.exception_toast),
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        private const val ARG: String = "arg"
        fun newInstance(id: Int) = ContactDetailsFragment().apply {
            arguments = bundleOf(
                ARG to id
            )
        }
    }
}