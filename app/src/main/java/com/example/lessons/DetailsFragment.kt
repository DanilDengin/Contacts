package com.example.lessons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf


class DetailsFragment : GetDetailsById, Fragment(R.layout.fragment_details) {

    private var ID: Int = -1
    private val handler = Handler(Looper.getMainLooper())
    private var name: TextView? = null
    private var number1: TextView? = null
    private var number2: TextView? = null
    private var email1: TextView? = null
    private var email2: TextView? = null
    private var description: TextView? = null

    companion object {
        private const val ARG: String = "arg"
        fun newInstance(id: Int) = DetailsFragment().apply {
            arguments = bundleOf(
                ARG to id
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name = requireView().findViewById(R.id.nameTextView)
        number1 = requireView().findViewById(R.id.number1TextView)
        number2 = requireView().findViewById(R.id.number2TextView)
        email1 = requireView().findViewById(R.id.eMail1TextView)
        email2 = requireView().findViewById(R.id.eMail2TextView)
        description = requireView().findViewById(R.id.descriptionTextView)
        val args = requireArguments()
        ID = args.getInt(ARG)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.supportActionBar?.setTitle(R.string.toolbar_details)
        mainActivity.contactService.getDetailsById(this, ID)
    }

    override fun getDetailsById(contactForDetails: Contact) {
        handler.post {
            name?.setText(contactForDetails.name)
            number1?.setText(contactForDetails.number1)
            number2?.setText(contactForDetails.number2)
            email1?.setText(contactForDetails.email1)
            email2?.setText(contactForDetails.email2)
            description?.setText(contactForDetails.description)
        }
    }

    override fun onDestroyView() {
        name = null
        number1 = null
        number2 = null
        email1 = null
        email2 = null
        description = null
        super.onDestroyView()
    }
}