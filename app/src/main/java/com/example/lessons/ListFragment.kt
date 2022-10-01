package com.example.lessons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment


class ListFragment : GetInformation, Fragment(R.layout.fragment_list) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.setTitle(R.string.toolbar_list)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.contactService.getContacts(this)
        val icon1: TextView = view.findViewById(R.id.contact1TextView)
        val icon2: TextView = view.findViewById(R.id.contact2TextView)
        icon1.setOnClickListener() {
            changeFragment(0)
        }
        icon2.setOnClickListener() {
            changeFragment(1)
        }
    }

    private fun changeFragment(ID: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(R.id.fragmentContainer, DetailsFragment.newInstance(ID.toString()))
            .addToBackStack("To Details")
            .commit()
    }

    override fun getList(contact1: Contact, contact2: Contact) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val name1: TextView? = view?.findViewById(R.id.name1ListTextView)
            val num1: TextView? = view?.findViewById(R.id.number1ListTextView)
            name1?.setText(contact1.name)
            num1?.setText(contact1.number1)
            val nameCont2: TextView? = view?.findViewById(R.id.name2ListTextView)
            val numCont2: TextView? = view?.findViewById(R.id.number2ListTextView)
            nameCont2?.setText(contact2.name)
            numCont2?.setText(contact2.number1)
        }
    }

    override fun getDetails(contForDetails: Contact) {
        TODO("Not yet implemented")
    }

}






