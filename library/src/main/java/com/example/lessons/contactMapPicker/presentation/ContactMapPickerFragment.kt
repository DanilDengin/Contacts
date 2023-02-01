package com.example.lessons.contactMapPicker.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactMap.di.DaggerContactMapComponent
import com.example.lessons.contactMapPicker.presentation.recyclerView.ContactMapPickerAdapter
import com.example.lessons.di.contactMap.MapComponentDependencies
import com.example.lessons.di.contactMap.MapComponentDependenciesProvider
import com.example.lessons.presentation.MainActivity
import com.example.lessons.presentation.recyclerView.ContactItemDecorator
import com.example.lessons.utils.constans.FIRST_CONTACT_BUNDLE_KEY
import com.example.lessons.utils.constans.ROUTE_MAP_BUNDLE_KEY
import com.example.lessons.utils.constans.ROUTE_MAP_KEY
import com.example.lessons.utils.constans.SECOND_CONTACT_BUNDLE_KEY
import com.example.lessons.utils.delegate.unsafeLazy
import com.example.lessons.utils.di.getDependenciesProvider
import com.example.lessons.utils.viewModel.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentContactMapPickerBinding
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.launch

internal class ContactMapPickerFragment : Fragment(R.layout.fragment_contact_map_picker) {

    @Inject
    lateinit var viewModelProvider: Provider<ContactMapPickerViewModel>

    private val binding by viewBinding(FragmentContactMapPickerBinding::bind)

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    private var selectedType: String? = null

    private val contactMapPickerAdapter: ContactMapPickerAdapter by unsafeLazy {
        ContactMapPickerAdapter(::changeContactMap, ::sendData, ::initRadioGroupListener)
    }

    override fun onAttach(context: Context) {
        DaggerContactMapComponent.builder()
            .mapComponentDependencies(
                requireContext()
                    .getDependenciesProvider<MapComponentDependenciesProvider>() as? MapComponentDependencies
            )
            .build()
            .also { it.inject(this) }
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        val recyclerView: RecyclerView = binding.contactMapRecyclerView
        val horizontalISpaceItemDecorator = ContactItemDecorator()
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
        recyclerView.adapter = contactMapPickerAdapter
        viewModel.getAllContactMaps()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactMapPickerList.collect(contactMapPickerAdapter::submitList)
        }
        viewModel.listSize.observe(viewLifecycleOwner) { size ->
            if (size >= SELECT_LIST_ALLOWED_SIZE)
                viewModel.approvalData()
        }
    }


    private fun checkDataValidation(plotRouteButton: Button): Unit {
        if (viewModel.selectedList.size >= SELECT_LIST_ALLOWED_SIZE && selectedType != null) {
            plotRouteButton.isEnabled = true
        }
    }

    private fun changeContactMap(contactMapPickerId: String, selected: Boolean) {
        viewModel.changeItem(contactMapPickerId, selected)
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

    private fun initRadioGroupListener(selectedRadioButton: String) {
        selectedType = selectedRadioButton
    }

    private fun sendData(unit: Unit) {
        parentFragmentManager.setFragmentResult(
            ROUTE_MAP_KEY,
            bundleOf(
                ROUTE_MAP_BUNDLE_KEY to selectedType,
                FIRST_CONTACT_BUNDLE_KEY to viewModel.selectedList[0],
                SECOND_CONTACT_BUNDLE_KEY to viewModel.selectedList[1]
            )
        )
        parentFragmentManager.popBackStack()
    }

    private companion object {
        const val SELECT_LIST_ALLOWED_SIZE = 2
    }
}