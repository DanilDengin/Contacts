package com.example.lessons

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView


class ListFragment : Fragment(R.layout.fragment_list) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity?)?.supportActionBar?.setTitle(R.string.toolbar_list)
        val icon: TextView = view.findViewById(R.id.contact1)
        icon.setOnClickListener() {
            changeFragment()
        }
    }


    private fun changeFragment() {
        val transaction = parentFragmentManager.beginTransaction()
        transaction
            .replace(R.id.fragment_container, DetailsFragment.newInstance(R.id.contact1.toString()))
            .addToBackStack(null)
            .commit()
    }
}






