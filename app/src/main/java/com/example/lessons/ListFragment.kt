package com.example.lessons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment


class ListFragment : GetContactList, Fragment(R.layout.fragment_list) {

    private val handler = Handler(Looper.getMainLooper())
    private var numberContact0: TextView? = null
    private var nameContact0: TextView? = null
    private var nameContact1: TextView? = null
    private var numberContact1: TextView? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberContact0 = requireView().findViewById(R.id.number0ListTextView)
        nameContact0 = requireView().findViewById(R.id.name0ListTextView)
        nameContact1 = requireView().findViewById(R.id.name1ListTextView)
        numberContact1 = requireView().findViewById(R.id.number1ListTextView)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        mainActivity.contactService.getContacts(this)

        val icon0: TextView = view.findViewById(R.id.contact0TextView)
        val icon1: TextView = view.findViewById(R.id.contact1TextView)
        icon0.setOnClickListener() {
            changeFragment(0)
        }
        icon1.setOnClickListener() {
            changeFragment(1)
        }
    }

    private fun changeFragment(id: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(R.id.fragmentContainer, DetailsFragment.newInstance(id))
            .addToBackStack("toDetails")
            .commit()
    }

    override fun getContactList(contacts: Array<Contact>) {
        handler.post {
            nameContact0?.text = contacts[0].name
            numberContact0?.text = contacts[0].number1
            nameContact1?.text = contacts[1].name
            numberContact1?.text = contacts[1].number1
        }
    }

    override fun onDestroyView() {
        numberContact0 = null
        nameContact0 = null
        numberContact1 = null
        nameContact1 = null
        super.onDestroyView()
    }

}






