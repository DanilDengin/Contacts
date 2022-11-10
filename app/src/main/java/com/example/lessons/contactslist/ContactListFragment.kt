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
import com.example.lessons.MainActivity
import com.example.lessons.R
import com.example.lessons.contactdetails.DetailsFragment
import com.example.lessons.contactslist.adapter.ContactListAdapter


class ContactListFragment : Fragment(R.layout.fragment_list) {

    private val viewModel: ContactListViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(
            this,
            ContactListViewModelFactory(requireActivity().applicationContext)
        )[ContactListViewModel::class.java]
    }
    private val contactsListAdapter: ContactListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        ContactListAdapter { id -> changeFragment(id = id) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mainActivity: MainActivity = activity as MainActivity
        setHasOptionsMenu(true)
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerView)
        val horizontalISpaceItemDecorator = ContactListItemDecorator()
        val layoutManager = LinearLayoutManager(context)
        viewModel.getUsers().observe(viewLifecycleOwner) { contactList ->
            contactsListAdapter.submitList(contactList)
        }
        recyclerView.adapter = contactsListAdapter
        layoutManager.recycleChildrenOnDetach = true
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(horizontalISpaceItemDecorator)
    }

    private fun changeFragment(id: String?) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(
                R.id.fragmentContainer,
                DetailsFragment.newInstance(requireNotNull(id).toInt())
            )
            .addToBackStack("toDetails")
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

}