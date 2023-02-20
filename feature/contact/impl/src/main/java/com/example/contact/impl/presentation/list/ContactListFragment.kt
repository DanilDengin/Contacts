package com.example.contact.impl.presentation.list

import com.example.contact.impl.R as FutureRes
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.contact.impl.databinding.FragmentListBinding
import com.example.contact.impl.presentation.ContactComponentViewModel
import com.example.contact.impl.presentation.ContactsComponentDependenciesProvider
import com.example.contact.impl.presentation.list.recyclerView.ContactListAdapter
import com.example.di.dependency.findFeatureExternalDeps
import com.example.mvvm.getRootViewModel
import com.example.mvvm.viewModel
import com.example.ui.R
import com.example.ui.recyclerView.ContactItemDecorator
import com.example.utils.delegate.unsafeLazy
import com.example.utils.idlingResource.TestIdlingResource
import javax.inject.Inject
import javax.inject.Provider

class ContactListFragment : Fragment(FutureRes.layout.fragment_list) {

    @VisibleForTesting
    val idlingResource = TestIdlingResource()

    @Inject
    lateinit var viewModelProvider: Provider<ContactListViewModel>

    private val searchViewHint by unsafeLazy {
        getString(R.string.hint_search_view)
    }

    private val binding by viewBinding(FragmentListBinding::bind)

    private val contactsListAdapter: ContactListAdapter by unsafeLazy {
        ContactListAdapter { id -> navigateToDetailsFragment(id = id) }
    }

    private val viewModel by unsafeLazy { viewModel { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        ContactsComponentDependenciesProvider.featureDependencies = findFeatureExternalDeps()
        getRootViewModel<ContactComponentViewModel>().component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        with(binding.contactListRecyclerView) {
            val horizontalSpaceItemDecorator = ContactItemDecorator()
            adapter = contactsListAdapter
            addItemDecoration(horizontalSpaceItemDecorator)
        }
        viewModel.users.observe(viewLifecycleOwner, contactsListAdapter::submitList)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.exceptionState.observe(viewLifecycleOwner) { showExceptionToast() }
    }

    private fun initActionBar() {
        requireActivity().title = getString(R.string.contact_list_toolbar)
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.contact_list_menu, menu)
                val searchView: SearchView =
                    menu.findItem(R.id.searchView).actionView as SearchView
                searchView.queryHint = searchViewHint
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        viewModel.filterUsers(query = query)
                        return false
                    }

                    override fun onQueryTextChange(query: String): Boolean {
                        viewModel.filterUsers(query = query)
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.themePicker -> {
                        navigateToThemePickerFragment()
                        true
                    }
                    R.id.mapView -> {
                        navigateToMapFragment()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.STARTED)
    }


    private fun navigateToThemePickerFragment() {
        viewModel.navigateToThemePickerFragment()
    }

    private fun navigateToMapFragment() {
        viewModel.navigateToMapFragment()
    }

    private fun navigateToDetailsFragment(id: String) {
        viewModel.navigateToDetails(id)
    }

    private fun setLoadingIndicator(isVisible: Boolean) {
        binding.progressBarList.isVisible = isVisible
        idlingResource.setIdleState(!isVisible)
    }

    private fun showExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.exception_toast),
            Toast.LENGTH_LONG
        ).show()
    }
}