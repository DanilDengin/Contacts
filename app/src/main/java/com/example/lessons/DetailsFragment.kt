package com.example.lessons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf


class DetailsFragment : GetInformation, Fragment(R.layout.fragment_details) {

    companion object {
        private const val ARG: String = "arg"
        fun newInstance(text: String) = DetailsFragment().apply {
            arguments = bundleOf(
                ARG to text
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()
        val text: TextView = view.findViewById(R.id.photoTextView)
        text.setText(args.getString(ARG, ""))
        (activity as MainActivity?)?.supportActionBar?.setTitle(R.string.toolbar_details)
        val photo: TextView = view.findViewById(R.id.photoTextView)
        val ID: Int = photo.text.toString().toInt()
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.contactService.getDetailsByID(this, ID)
    }

    override fun getList(contact1: Contact, contact2: Contact) {
        TODO("Not yet implemented")
    }

    override fun getDetails(contForDetails: Contact) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val name: TextView? = view?.findViewById(R.id.nameTextView)
            val num1: TextView? = view?.findViewById(R.id.number1TextView)
            val num2: TextView? = view?.findViewById(R.id.number2TextView)
            val email1: TextView? = view?.findViewById(R.id.eMail1TextView)
            val email2: TextView? = view?.findViewById(R.id.eMail2TextView)
            val description: TextView? = view?.findViewById(R.id.descriptionTextView)
            name?.setText(contForDetails.name)
            num1?.setText(contForDetails.number1)
            num2?.setText(contForDetails.number2)
            email1?.setText(contForDetails.email1)
            email2?.setText(contForDetails.email2)
            description?.setText(contForDetails.description)
        }
    }

}