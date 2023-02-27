package com.example.contact.impl.presentation.details

import com.example.contact.impl.R as FutureRes
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contact.impl.databinding.FragmentDetailsBinding
import com.example.contact.impl.domain.entity.ContactDetails
import com.example.contact.impl.presentation.ContactComponentViewModel
import com.example.contact.impl.presentation.ContactsComponentDependenciesProvider
import com.example.di.dependency.findFeatureExternalDeps
import com.example.mvvm.getComponentViewModel
import com.example.mvvm.viewModel
import com.example.ui.R
import com.example.utils.delegate.unsafeLazy
import com.example.utils.idlingResource.TestIdlingResource
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch

internal class ContactDetailsFragment : Fragment(FutureRes.layout.fragment_details) {

    @VisibleForTesting
    val idlingResource = TestIdlingResource()

    @Inject
    lateinit var viewModelFactoryProvider: Provider<ContactDetailsViewModel.Factory>

    @Inject
    lateinit var birthdayDelegateFactoryProvider: Provider<BirthdayDelegate.Factory>

    private val birthdayDelegate by unsafeLazy {
        birthdayDelegateFactoryProvider.get().create(contactId)
    }

    private val binding by viewBinding(FragmentDetailsBinding::bind)

    private var contactId: Int = -1

    private val viewModel by unsafeLazy {
        viewModel {
            viewModelFactoryProvider.get().create(contactId.toString())
        }
    }

    override fun onAttach(context: Context) {
        ContactsComponentDependenciesProvider.contactsExternalDependencies =
            findFeatureExternalDeps()
        getComponentViewModel<ContactComponentViewModel>().contactsComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.contact_details_toolbar)
        contactId = requireArguments().getInt(ARG)
        viewModel.loadUserDetail()
        initObservers()
        binding.mapButton.setOnClickListener {
            navigateToContactMapFragment()
        }
        checkBirthdaySwitchState()
        initBirthdayChangeListener()
    }

    private fun initObservers() {
        viewModel.user.observe(viewLifecycleOwner, ::updateView)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.exceptionState.observe(viewLifecycleOwner) { showExceptionToast() }
    }

    private fun initBirthdayChangeListener() {
        binding.birthdaySwitch.setOnCheckedChangeListener { _, isChecked ->
            when (isChecked) {
                true -> {
                    Toast.makeText(
                        context,
                        getString(R.string.remind_birthday_toast),
                        Toast.LENGTH_LONG
                    ).show()
                    doAlarm()
                }
                false -> {
                    Toast.makeText(
                        context,
                        getString(R.string.cancel_birthday_remind_toast),
                        Toast.LENGTH_LONG
                    )
                    birthdayDelegate.cancelAlarm()
                }
            }
        }
    }

    private fun checkBirthdaySwitchState() {
        binding.birthdaySwitch.isChecked =
            birthdayDelegate.checkBirthdaySwitchState()
    }


    private fun navigateToContactMapFragment() {
        viewModel.navigateToMapFragment()
    }

    private fun updateView(contactForDetails: ContactDetails?) {
        with(binding) {
            contactForDetails?.also { contact ->
                detailsFragmentContainer.visibility = View.VISIBLE
                nameTextView.text = contact.name
                number1TextView.text = contact.numberPrimary
                number2TextView.text = contact.numberSecondary
                eMail1TextView.text = contact.emailPrimary
                eMail2TextView.text = contact.emailSecondary
                addressTextView.text = contact.address
                number2TextView.isGone = contact.numberSecondary == null
                number2ImageView.isGone = contact.numberSecondary == null
                eMail1TextView.isGone = contact.emailPrimary == null
                email1ImageView.isGone = contact.emailPrimary == null
                eMail2TextView.isGone = contact.emailSecondary == null
                email2ImageView.isGone = contact.emailSecondary == null
                addressTextView.isGone = contact.address == null
                birthdayTextView.text = contact.birthday.orEmpty()
                birthdaySwitch.isClickable = contact.birthday.isNullOrEmpty().not()
                birthdaySwitch.isVisible = contact.birthday.isNullOrEmpty().not()
                birthdayDelegate.setContactName(nameTextView.text.toString())
            }
        }
    }

    private fun doAlarm() {
        viewLifecycleOwner.lifecycleScope.launch {
            birthdayDelegate.doAlarm(
                viewModel.getAlarmDate()
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