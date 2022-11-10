package com.example.lessons.contactslist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.Contact
import com.example.lessons.R

class ContactsListViewHolder(itemView: View, changeFragment: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.nameListTextView)
    private val number: TextView = itemView.findViewById(R.id.numberListTextView)

    init {
        itemView.setOnClickListener {
            val adapterPosition = absoluteAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                changeFragment(adapterPosition)
            }
        }
    }

    fun bind(contact: Contact) {
        name.text = contact.name
        number.text = contact.number1
    }
}