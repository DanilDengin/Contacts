package com.example.lessons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf


class FragmentDetails : Fragment() {

    companion object {
        fun newInstance (text: String) = FragmentDetails().apply {
            arguments= bundleOf(
                "Arg" to text
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()
        val text: TextView = view.findViewById(R.id.SetPhoto)
        text.setText(args.getString("Arg", "" ))
    }
}