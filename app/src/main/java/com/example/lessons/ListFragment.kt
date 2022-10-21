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
    private var icon0: TextView? = null
    private var nameContact1: TextView? = null
    private var numberContact1: TextView? = null
    private var icon1: TextView? = null
    private var id0: String? = null
    private var id1: String? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberContact0 = requireView().findViewById(R.id.number0ListTextView)
        nameContact0 = requireView().findViewById(R.id.name0ListTextView)
        nameContact1 = requireView().findViewById(R.id.name1ListTextView)
        numberContact1 = requireView().findViewById(R.id.number1ListTextView)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_list)
        icon0 = view.findViewById(R.id.contact0TextView)
        icon1 = view.findViewById(R.id.contact1TextView)
        mainActivity.contactService.getContacts(this, requireContext())
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

    override fun getContactList(contacts: ArrayList<Contact>) {
        handler.post {
            nameContact0?.text = contacts[2].name
            numberContact0?.text = contacts[2].number1
            id0 = contacts[2].id
            nameContact1?.text = contacts[0].name
            numberContact1?.text = contacts[0].number1
            id1 = contacts[0].id
            icon0?.setOnClickListener {
                changeFragment(id0)
            }
            icon1?.setOnClickListener {
                changeFragment(id1)
            }
        }
    }


    override fun onDestroyView() {
        numberContact0 = null
        nameContact0 = null
        numberContact1 = null
        nameContact1 = null
        icon0 = null
        icon1 = null
        handler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

}






