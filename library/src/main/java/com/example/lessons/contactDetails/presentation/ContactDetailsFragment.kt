package com.example.lessons.contactDetails.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactDetails.di.DaggerContactDetailsComponent
import com.example.lessons.contactDetails.presentation.BirthdayReceiver.Companion.BIRTHDAY_CONTACT_ID_INTENT_KEY
import com.example.lessons.contactDetails.presentation.BirthdayReceiver.Companion.BIRTHDAY_CONTACT_NAME_INTENT_KEY
import com.example.lessons.contactDetails.presentation.BirthdayReceiver.Companion.BIRTHDAY_RECEIVER_INTENT_ACTION
import com.example.lessons.contactMap.data.model.toArguments
import com.example.lessons.contactMap.presentation.ContactMapFragment
import com.example.lessons.contacts.domain.entity.Contact
import com.example.lessons.di.contactListDetails.ContactComponentDependencies
import com.example.lessons.presentation.mainActivity.MainActivity
import com.example.lessons.utils.delegate.unsafeLazy
import com.example.lessons.utils.di.getAppDependenciesProvider
import com.example.lessons.utils.viewModel.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentDetailsBinding
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Calendar.YEAR
import java.util.GregorianCalendar
import java.util.StringJoiner
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch

internal class ContactDetailsFragment : Fragment(R.layout.fragment_details) {

    @Inject
    lateinit var viewModelFactoryProvider: Provider<ContactDetailsViewModel.Factory>

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
        DaggerContactDetailsComponent.builder()
            .contactComponentDependencies(
                requireContext().getAppDependenciesProvider<ContactComponentDependencies>()
            )
            .build()
            .also { it.inject(this) }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactId = requireArguments().getInt(ARG)
        initActionBar()
        viewModel.user.observe(viewLifecycleOwner, ::updateView)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.exceptionState.observe(viewLifecycleOwner) { showExceptionToast() }

        intentBirthday.setClass(requireContext(), BirthdayReceiver::class.java)
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
        parentFragmentManager.commit {
            replace(
                R.id.fragmentContainer,
                ContactMapFragment.newInstance(viewModel.user.value?.toArguments())
            )
            setReorderingAllowed(true)
            addToBackStack(CONTACT_MAP_FRAGMENT_BACK_STACK_KEY)
        }
    }

    private fun initActionBar() {
        (activity as? MainActivity)?.also { activity ->
            activity.addMenuProvider(object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.backstack_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return if (menuItem.itemId == android.R.id.home) {
                        parentFragmentManager.popBackStack()
                        true
                    } else {
                        false
                    }
                }
            }, viewLifecycleOwner, Lifecycle.State.STARTED)
            activity.supportActionBar?.also { actionBar ->
                actionBar.setTitle(R.string.contact_details_toolbar)
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun updateView(contactForDetails: Contact) {
        with(binding) {
            nameTextView.text = contactForDetails.name
            number1TextView.text = contactForDetails.number1
            number2TextView.text = contactForDetails.number2
            eMail1TextView.text = contactForDetails.email1
            eMail2TextView.text = contactForDetails.email2
            descriptionTextView.text = contactForDetails.description
            number2TextView.isGone = contactForDetails.number2 == null
            number2ImageView.isGone = contactForDetails.number2 == null
            eMail1TextView.isGone = contactForDetails.email1 == null
            email1ImageView.isGone = contactForDetails.email1 == null
            eMail2TextView.isGone = contactForDetails.email2 == null
            email2ImageView.isGone = contactForDetails.email2 == null
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
        val CONTACT_MAP_FRAGMENT_BACK_STACK_KEY: String =
            ContactMapFragment::class.java.simpleName
        private const val ARG: String = "arg"
        fun newInstance(id: Int) = ContactDetailsFragment().apply {
            arguments = bundleOf(
                ARG to id
            )
        }
    }
}