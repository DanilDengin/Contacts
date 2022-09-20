package com.example.lessons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf


class DetailsFragment : Fragment(R.layout.fragment_details) {

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
    }

}