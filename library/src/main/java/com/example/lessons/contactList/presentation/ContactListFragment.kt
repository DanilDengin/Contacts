package com.example.lessons.contactList.presentation

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.contactDetails.presentation.ContactDetailsFragment
import com.example.lessons.contactList.di.DaggerContactListComponent
import com.example.lessons.contactList.presentation.recyclerView.ContactListAdapter
import com.example.lessons.contactList.presentation.recyclerView.ContactListItemDecorator
import com.example.lessons.contactMap.presentation.ContactMapFragment
import com.example.lessons.presentation.MainActivity
import com.example.lessons.themePicker.presentation.ThemePickerFragment
import com.example.lessons.utils.delegate.unsafeLazy
import com.example.lessons.utils.di.getComponentDependencies
import com.example.lessons.utils.viewModel.viewModel
import com.example.library.R
import com.example.library.databinding.FragmentListBinding
import javax.inject.Inject
import javax.inject.Provider

internal class ContactListFragment : Fragment(R.layout.fragment_list) {

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
        super.onAttach(context)
        DaggerContactListComponent.builder()
            .contactComponentDependencies(requireContext().getComponentDependencies())
            .build()
            .also { it.inject(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionBar()
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val horizontalISpaceItemDecorator = ContactListItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        viewModel.users.observe(viewLifecycleOwner, contactsListAdapter::submitList)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.exceptionState.observe(viewLifecycleOwner) { showExceptionToast() }
        recyclerView.adapter = contactsListAdapter
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
    }

    private fun initActionBar() {
        (activity as? MainActivity)?.also { activity ->
            activity.addMenuProvider(object : MenuProvider {
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
            activity.supportActionBar?.also { actionBar ->
                actionBar.setTitle(R.string.contact_list_toolbar)
                actionBar.setDisplayHomeAsUpEnabled(false)
            }
        }
    }

    private fun navigateToThemePickerFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ThemePickerFragment())
            .addToBackStack(THEME_PICKER_FRAGMENT_BACK_STACK_KEY)
            .commit()
    }

    private fun navigateToMapFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, ContactMapFragment())
            .addToBackStack(CONTACT_MAP_FRAGMENT_BACK_STACK_KEY)
            .commit()
    }

    private fun navigateToDetailsFragment(id: String) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ContactDetailsFragment.newInstance(id.toInt())
            )
            .addToBackStack(CONTACT_DETAILS_FRAGMENT_BACK_STACK_KEY)
            .commit()
    }

    private fun setLoadingIndicator(isVisible: Boolean) {
        binding.progressBarList.isVisible = isVisible
    }

    private fun showExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.exception_toast),
            Toast.LENGTH_LONG
        ).show()
    }

    private companion object {
        val CONTACT_DETAILS_FRAGMENT_BACK_STACK_KEY: String =
            ContactDetailsFragment::class.java.simpleName
        val THEME_PICKER_FRAGMENT_BACK_STACK_KEY: String =
            ThemePickerFragment::class.java.simpleName
        val CONTACT_MAP_FRAGMENT_BACK_STACK_KEY: String =
            ContactMapFragment::class.java.simpleName
    }
}