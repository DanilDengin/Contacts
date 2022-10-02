package com.example.lessons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment


class ListFragment :GetContactList, Fragment(R.layout.fragment_list) {

    val handler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.setTitle(R.string.toolbar_list)
        val mainActivity: MainActivity = activity as MainActivity
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

    private fun changeFragment(Id: Int) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(R.id.fragmentContainer, DetailsFragment.newInstance(Id))
            .addToBackStack("To Details")
            .commit()
    }

    override fun getContactList(contacts: Array<Contact>) {
        handler.post {
            val nameContact0: TextView? by lazy { view?.findViewById(R.id.name0ListTextView)}
            val numberContact0: TextView? by lazy { view?.findViewById(R.id.number0ListTextView)}
            nameContact0?.setText(contacts[0].name)
            numberContact0?.setText(contacts[0].number1)
            val nameContact1: TextView? by lazy { view?.findViewById(R.id.name1ListTextView)}
            val numberContact1: TextView? by lazy { view?.findViewById(R.id.number1ListTextView)}
            nameContact1?.setText(contacts[1].name)
            numberContact1?.setText(contacts[1].number1)
        }
    }

}






