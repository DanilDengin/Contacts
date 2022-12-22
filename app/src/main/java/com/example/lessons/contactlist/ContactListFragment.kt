package com.example.lessons.contactlist

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.lessons.App
import com.example.lessons.MainActivity
import com.example.lessons.R
import com.example.lessons.contactdetails.ContactDetailsFragment
import com.example.lessons.contactlist.adapter.ContactListAdapter
import com.example.lessons.databinding.FragmentListBinding
import com.example.lessons.di.DaggerContactListComponent
import com.example.lessons.viewModel
import javax.inject.Inject
import javax.inject.Provider

class ContactListFragment : Fragment(R.layout.fragment_list) {

    private companion object {
        const val CONTACT_DETAILS_FRAGMENT_BACK_STACK_KEY = "BirthdayDetailsFragment"
        const val SEARCH_VIEW_HINT = "Search"
    }

    private val binding by viewBinding(FragmentListBinding::bind)

    @Inject
    lateinit var viewModelProvider: Provider<ContactListViewModel>

    private val contactsListAdapter: ContactListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ContactListAdapter { id -> navigateToDetailsFragment(id = id) }
    }
    private val viewModel by lazy(LazyThreadSafetyMode.NONE) { this.viewModel { viewModelProvider.get() } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val contactListComponent = DaggerContactListComponent.builder()
            .appComponent((requireContext().applicationContext as App).appComponent)
            .build()
            .also { it.inject(this) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val horizontalISpaceItemDecorator = ContactListItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        viewModel.users.observe(viewLifecycleOwner, contactsListAdapter::submitList)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
        viewModel.getExceptionState().observe(viewLifecycleOwner) { showExceptionToast() }
        recyclerView.adapter = contactsListAdapter
        layoutManager.recycleChildrenOnDetach = true
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
    }

    private fun navigateToDetailsFragment(id: String?) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fragmentContainer,
                ContactDetailsFragment.newInstance(requireNotNull(id).toInt())
            )
            .addToBackStack(CONTACT_DETAILS_FRAGMENT_BACK_STACK_KEY)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchView: SearchView = menu.findItem(R.id.searchView).actionView as SearchView
        searchView.queryHint = SEARCH_VIEW_HINT
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterUsers(query = query)
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.filterUsers(query = query)
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setLoadingIndicator(isVisible: Boolean) {
        binding.progressBarList.isVisible = isVisible
    }

    private fun showExceptionToast() {
        val contextNotNull = requireContext()
        Toast.makeText(
            contextNotNull,
            contextNotNull.getText(R.string.toast_exception),
            Toast.LENGTH_LONG
        ).show()
    }
}