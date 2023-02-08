package com.example.lessons.contactMapPicker.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactMap.di.DaggerContactMapComponent
import com.example.lessons.contactMap.presentation.ContactMapFragment.Companion.FIRST_CONTACT_BUNDLE_KEY
import com.example.lessons.contactMap.presentation.ContactMapFragment.Companion.ROUTE_MAP_BUNDLE_KEY
import com.example.lessons.contactMap.presentation.ContactMapFragment.Companion.ROUTE_MAP_KEY
import com.example.lessons.contactMap.presentation.ContactMapFragment.Companion.SECOND_CONTACT_BUNDLE_KEY
import com.example.lessons.contactMapPicker.data.model.ContactMapPicker
import com.example.lessons.contactMapPicker.presentation.ContactMapPickerViewModel.Companion.SELECT_LIST_ALLOWED_SIZE
import com.example.lessons.contactMapPicker.presentation.recyclerView.ContactMapPickerAdapter
import com.example.lessons.di.contactMap.MapComponentDependencies
import com.example.lessons.presentation.mainActivity.MainActivity
import com.example.lessons.presentation.recyclerView.ContactItemDecorator
import com.example.lessons.utils.delegate.unsafeLazy
import com.example.lessons.utils.di.getDataDependenciesProvider
import com.example.lessons.utils.viewModel.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentContactMapPickerBinding
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class ContactMapPickerFragment : Fragment(R.layout.fragment_contact_map_picker) {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapPickerViewModel>

    private val binding by viewBinding(FragmentContactMapPickerBinding::bind)

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    private var selectedRouteType: String? = null

    private val contactMapPickerAdapter: ContactMapPickerAdapter by unsafeLazy {
        ContactMapPickerAdapter(::chooseContact, ::sendData, ::selectRoute)
    }

    override fun onAttach(context: Context) {
        DaggerContactMapComponent.factory()
            .create(requireContext().getDataDependenciesProvider())
            .inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        with(binding.contactMapRecyclerView) {
            val horizontalSpaceItemDecorator = ContactItemDecorator()
            addItemDecoration(horizontalSpaceItemDecorator)
            adapter = contactMapPickerAdapter
        }
        viewModel.getAllContactMaps()
        viewModel.contactMapPickerList
            .onEach(contactMapPickerAdapter::submitList)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun chooseContact(contactMapPicker: ContactMapPicker, selected: Boolean) {
        viewModel.changeContactSelection(contactMapPicker, selected)
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
                actionBar.setTitle(R.string.contact_map_picker_toolbar)
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun selectRoute(selectedRadioButton: String) {
        selectedRouteType = selectedRadioButton
    }

    private fun sendData() {
        if (selectedRouteType != null && viewModel.selectedContactList.size == SELECT_LIST_ALLOWED_SIZE) {
            parentFragmentManager.setFragmentResult(
                ROUTE_MAP_KEY,
                bundleOf(
                    ROUTE_MAP_BUNDLE_KEY to selectedRouteType,
                    FIRST_CONTACT_BUNDLE_KEY to viewModel.selectedContactList[0].id,
                    SECOND_CONTACT_BUNDLE_KEY to viewModel.selectedContactList[1].id
                )
            )
            parentFragmentManager.popBackStack()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.not_valid_data_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}