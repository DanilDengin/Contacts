package com.example.lessons.contactlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import javax.inject.Inject

class ContactListFragment : Fragment(R.layout.fragment_list) {

    @Inject
    lateinit var contactListViewModelFactory: ContactListViewModelFactory

    private val binding by viewBinding(FragmentListBinding::bind)

    private val viewModel: ContactListViewModel by viewModels { contactListViewModelFactory }

    private val contactsListAdapter: ContactListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ContactListAdapter { id -> navigateToDetailsFragment(id = id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactListComponent = DaggerContactListComponent.builder()
            .appComponent((requireContext().applicationContext as App).appComponent)
            .build()
        contactListComponent.inject(this)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        val horizontalISpaceItemDecorator = ContactListItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        viewModel.users.observe(viewLifecycleOwner, contactsListAdapter::submitList)
        viewModel.progressBarState.observe(viewLifecycleOwner, ::setLoadingIndicator)
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
            .addToBackStack("ContactDetailsFragment")
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchView: SearchView = menu.findItem(R.id.searchView).actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.filterUsers(query = query, requireContext())
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                viewModel.filterUsers(query = query, requireContext())
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setLoadingIndicator(isVisible: Boolean) {
        binding.progressBarList.isVisible = isVisible
    }
}