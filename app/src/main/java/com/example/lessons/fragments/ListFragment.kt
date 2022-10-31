package com.example.lessons.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.lessons.Contact
import com.example.lessons.GetContactList
import com.example.lessons.MainActivity
import com.example.lessons.R
import com.example.lessons.viewmodels.ViewModelForList


class ListFragment : GetContactList, Fragment(R.layout.fragment_list) {

    private var numberContact0: TextView? = null
    private var nameContact0: TextView? = null
    private var icon0: TextView? = null
    private var nameContact1: TextView? = null
    private var numberContact1: TextView? = null
    private var icon1: TextView? = null
    private var id0: String? = null
    private var id1: String? = null
    private val viewModel: ViewModelForList by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this)[ViewModelForList::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberContact0 = requireView().findViewById(R.id.number0ListTextView)
        nameContact0 = requireView().findViewById(R.id.name0ListTextView)
        nameContact1 = requireView().findViewById(R.id.name1ListTextView)
        numberContact1 = requireView().findViewById(R.id.number1ListTextView)
        icon0 = requireView().findViewById(R.id.contact0TextView)
        icon1 = requireView().findViewById(R.id.contact1TextView)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        viewModel.getUsers(requireContext()).observe(
            viewLifecycleOwner
        ) { users ->
            getContactList(users)
        }
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

    override fun getContactList(contacts: List<Contact>?) {
        val contactsNotNull = requireNotNull(contacts)
        nameContact0?.text = contactsNotNull[2].name
        numberContact0?.text = contactsNotNull[2].number1
        id0 = contactsNotNull[2].id
        nameContact1?.text = contactsNotNull[0].name
        numberContact1?.text = contactsNotNull[0].number1
        id1 = contactsNotNull[0].id
        icon0?.setOnClickListener {
            changeFragment(id0)
        }
        icon1?.setOnClickListener {
            changeFragment(id1)
        }
    }


    override fun onDestroyView() {
        numberContact0 = null
        nameContact0 = null
        numberContact1 = null
        nameContact1 = null
        icon0 = null
        icon1 = null
        super.onDestroyView()
    }

    override fun onStart() {
        super.onStart()
        Log.e("A", "Fragment onStart")
    }

}






