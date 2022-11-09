package com.example.lessons.contactslist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.Contact
import com.example.lessons.MainActivity
import com.example.lessons.R
import com.example.lessons.contactdetails.DetailsFragment
import com.example.lessons.contactslist.adapter.ContactsListAdapter


class ContactsListFragment : SetContactList, Fragment(R.layout.fragment_list) {

    private val viewModel: ContactsListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ContactsListViewModelFactory(requireActivity().applicationContext)
        )[ContactsListViewModel::class.java]
    }
    private val contactsListAdapter: ContactsListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ContactsListAdapter(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        val horizontalISpaceItemDecorator = ContactsListItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        viewModel.getUsers().observe(viewLifecycleOwner, ::setContactList)
        recyclerView.adapter = contactsListAdapter
        layoutManager.recycleChildrenOnDetach = true
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
    }

    fun changeFragment(id: String?) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(
                R.id.fragmentContainer,
                DetailsFragment.newInstance(requireNotNull(id).toInt())
            )
            .addToBackStack("toDetails")
            .commit()
    }

    override fun setContactList(contactsList: List<Contact>?) {
        contactsListAdapter.setContactsList(contactsList)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        val searchView: SearchView = menu.findItem(R.id.searchView).actionView as SearchView
        searchView.queryHint = "Search"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null || query.trim() == "") {
                    viewModel.loadUsers(requireContext())
                }
                stringFilter(
                    oldContactList = viewModel.getUsers().value,
                    query = query, viewModel
                )
                return false
            }

            override fun onQueryTextChange(query: String?): Boolean {
                if (query == null || query.trim() == "") {
                    viewModel.loadUsers(requireContext())
                }
                stringFilter(
                    oldContactList = viewModel.getUsers().value,
                    query = query, viewModel
                )
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun stringFilter(
        oldContactList: List<Contact>?,
        query: String?,
        viewModel: ContactsListViewModel
    ) {
        Thread {
            val filteredContacts = ArrayList<Contact>()
            oldContactList?.forEach { contact ->
                if (contact.name.contains(query!!.trim(), ignoreCase = true))
                    filteredContacts.add(contact)
            }
            viewModel.updateUsers(filteredContacts)
        }.start()
    }
}