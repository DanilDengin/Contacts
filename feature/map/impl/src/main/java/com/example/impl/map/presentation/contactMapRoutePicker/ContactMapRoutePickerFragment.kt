package com.example.impl.map.presentation.contactMapRoutePicker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.di.dependency.findFeatureExternalDeps
import com.example.impl.map.databinding.FragmentContactMapRoutePickerBinding
import com.example.impl.map.domain.entity.ContactMapPicker
import com.example.impl.map.presentation.MapComponentDependenciesProvider
import com.example.impl.map.presentation.MapComponentViewModel
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.FIRST_CONTACT_BUNDLE_KEY
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.ROUTE_MAP_BUNDLE_KEY
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.ROUTE_MAP_KEY
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.SECOND_CONTACT_BUNDLE_KEY
import com.example.impl.map.presentation.contactMapRoutePicker.ContactMapRoutePickerViewModel.Companion.SELECT_LIST_ALLOWED_SIZE
import com.example.impl.map.presentation.contactMapRoutePicker.recyclerView.ContactMapPickerAdapter
import com.example.mvvm.getComponentViewModel
import com.example.mvvm.viewModel
import com.example.ui.R
import com.example.ui.recyclerView.ContactItemDecorator
import com.example.utils.delegate.unsafeLazy
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Provider
import com.example.impl.map.R as FeatureRes

internal class ContactMapRoutePickerFragment :
    Fragment(FeatureRes.layout.fragment_contact_map_route_picker) {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapRoutePickerViewModel>

    private val binding by viewBinding(FragmentContactMapRoutePickerBinding::bind)

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    private var selectedRouteType: String? = null

    private val contactMapPickerAdapter: ContactMapPickerAdapter by unsafeLazy {
        ContactMapPickerAdapter(::chooseContact, ::sendData, ::selectRoute)
    }

    override fun onAttach(context: Context) {
        MapComponentDependenciesProvider.mapExternalDependencies = findFeatureExternalDeps()
        getComponentViewModel<MapComponentViewModel>().mapComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().title = getString(R.string.contact_map_picker_toolbar)
        viewModel.getAllContactMaps()
        initRecyclerView()
        initObservers()
    }

    private fun initRecyclerView() {
        with(binding.contactMapRecyclerView) {
            val horizontalSpaceItemDecorator = ContactItemDecorator()
            addItemDecoration(horizontalSpaceItemDecorator)
            adapter = contactMapPickerAdapter
        }
    }

    private fun initObservers() {
        viewModel.contactMapPickerList
            .onEach(contactMapPickerAdapter::submitList)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun chooseContact(contactMapPicker: ContactMapPicker, selected: Boolean) {
        viewModel.changeContactSelection(contactMapPicker, selected)
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
            viewModel.exit()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.not_valid_data_toast),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
