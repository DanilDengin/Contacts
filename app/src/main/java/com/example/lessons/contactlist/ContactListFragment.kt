package com.example.lessons.contactlist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.App
import com.example.lessons.MainActivity
import com.example.lessons.R
import com.example.lessons.contactdetails.ContactDetailsFragment
import com.example.lessons.contactlist.adapter.ContactListAdapter
import com.example.lessons.di.ContactListModule
import com.example.lessons.di.DaggerContactListComponent
import javax.inject.Inject


class ContactListFragment : Fragment(R.layout.fragment_list) {


    @Inject
    lateinit var contactListViewModelFactory: ContactListViewModelFactory

    private val viewModel: ContactListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            contactListViewModelFactory
        )[ContactListViewModel::class.java]
    }
    private val contactsListAdapter: ContactListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ContactListAdapter { id -> navigateToDetailsFragment(id = id) }
    }
    private var progressBar: ProgressBar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val contactListComponent = DaggerContactListComponent.builder()
            .appComponent(App.appComponent)
            .contactListModule(ContactListModule())
            .build()
        contactListComponent.inject(this)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        val horizontalISpaceItemDecorator = ContactListItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        viewModel.getUsers().observe(viewLifecycleOwner, contactsListAdapter::submitList)
        viewModel.getProgressBarState().observe(viewLifecycleOwner, ::setLoadingIndicator)
        recyclerView.adapter = contactsListAdapter
        layoutManager.recycleChildrenOnDetach = true
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
        progressBar = requireView().findViewById(R.id.progressBarList)
    }

    private fun navigateToDetailsFragment(id: String?) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(
                R.id.fragmentContainer,
                ContactDetailsFragment.newInstance(requireNotNull(id).toInt())
            )
            .addToBackStack("navigateToDetailsFragment")
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
        progressBar?.isVisible = isVisible
    }

    override fun onDestroyView() {
        progressBar = null
        super.onDestroyView()
    }

}