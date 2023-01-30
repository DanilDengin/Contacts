package com.example.lessons.contactMapPicker.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactMap.di.DaggerContactMapComponent
import com.example.lessons.contactMapPicker.presentation.recyclerView.ContactMapPickerAdapter
import com.example.lessons.di.contactMap.MapComponentDependencies
import com.example.lessons.di.contactMap.MapComponentDependenciesProvider
import com.example.lessons.presentation.MainActivity
import com.example.lessons.presentation.recyclerView.ContactItemDecorator
import com.example.lessons.utils.constans.BUS_BUNDLE_PAIR
import com.example.lessons.utils.constans.CAR_BUNDLE_PAIR
import com.example.lessons.utils.constans.FIRST_CONTACT_BUNDLE_KEY
import com.example.lessons.utils.constans.FOOT_BUNDLE_PAIR
import com.example.lessons.utils.constans.MIXED_FORMAT_BUNDLE_PAIR
import com.example.lessons.utils.constans.ROUTE_MAP_BUNDLE_KEY
import com.example.lessons.utils.constans.ROUTE_MAP_KEY
import com.example.lessons.utils.constans.SECOND_CONTACT_BUNDLE_KEY
import com.example.lessons.utils.constans.UNDERGROUND_BUNDLE_PAIR
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

    private var filledRadio: Boolean = false

    private var selectedType: String? = null

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
        initRadioGroupListener()
        binding.plotRouteButton.setOnClickListener {
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
        val recyclerView: RecyclerView = binding.contactMapRecyclerView
        val horizontalISpaceItemDecorator = ContactItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllContactMaps()
            viewModel.contactMapPickerList.collect { contactMapPickerList ->
                if (contactMapPickerList != null) {
                    recyclerView.adapter =
                        ContactMapPickerAdapter(contactMapPickerList) { contactMapPickerId, selected ->
                            changeContactMap(contactMapPickerId, selected)
                        }
                }
            }
        }
        viewModel.dataValidation.observe(viewLifecycleOwner) {
            checkDataValidation()
        }
    }

    private fun checkDataValidation() {
        if (viewModel.selectedList.size >= 2 && filledRadio) {
            binding.plotRouteButton.isEnabled = true
        }
    }

    private fun changeContactMap(contactMapPickerId: String, selected: Boolean) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.changeItem(contactMapPickerId, selected)
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
                actionBar.setTitle(R.string.contact_map_picker_toolbar)
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initRadioGroupListener() {
        binding.radioGroupMapPicker.setOnCheckedChangeListener { _, checkedId ->
            filledRadio = true
            checkDataValidation()
            when (checkedId) {
                R.id.radioButtonBus ->selectedType = BUS_BUNDLE_PAIR
                R.id.radioButtonCar -> selectedType = CAR_BUNDLE_PAIR
                R.id.radioButtonFoot -> selectedType = FOOT_BUNDLE_PAIR
                R.id.radioButtonUnderground -> selectedType = UNDERGROUND_BUNDLE_PAIR
                R.id.radioButtonMixedFormat -> selectedType = MIXED_FORMAT_BUNDLE_PAIR
            }
        }
    }
}