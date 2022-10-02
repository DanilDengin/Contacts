package com.example.lessons

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf


class DetailsFragment : GetDetailsById, Fragment(R.layout.fragment_details) {

    var ID : Int = -1
    val handler = Handler(Looper.getMainLooper())

    companion object {
        private const val ARG: String = "arg"
        fun newInstance(Id: Int) = DetailsFragment().apply {
            arguments = bundleOf(
                ARG to Id
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()
        ID = args.getInt(ARG)
        (activity as MainActivity?)?.supportActionBar?.setTitle(R.string.toolbar_details)
        val mainActivity: MainActivity = activity as MainActivity
        mainActivity.contactService.getDetailsById(this, ID)
    }

    override fun getDetailsById(contactForDetails: Contact) {
        handler.post {
            val name: TextView?  by lazy { view?.findViewById(R.id.nameTextView)}
            val num1: TextView? by lazy { view?.findViewById(R.id.number1TextView) }
            val num2: TextView? by lazy { view?.findViewById(R.id.number2TextView) }
            val email1: TextView? by lazy { view?.findViewById(R.id.eMail1TextView)}
            val email2: TextView? by lazy { view?.findViewById(R.id.eMail2TextView)}
            val description: TextView? by lazy { view?.findViewById(R.id.descriptionTextView)}
            name?.setText(contactForDetails.name)
            num1?.setText(contactForDetails.number1)
            num2?.setText(contactForDetails.number2)
            email1?.setText(contactForDetails.email1)
            email2?.setText(contactForDetails.email2)
            description?.setText(contactForDetails.description)
        }
    }

}