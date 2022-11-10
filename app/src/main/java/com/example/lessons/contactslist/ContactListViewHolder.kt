package com.example.lessons.contactslist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.Contact
import com.example.lessons.R

class ContactListViewHolder(itemView: View, navigateToContactDetailsById: (Int) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.nameListTextView)
    private val number: TextView = itemView.findViewById(R.id.numberListTextView)

    init {
        itemView.setOnClickListener {
            val adapterPosition = bindingAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                navigateToContactDetailsById(adapterPosition)
            }
        }
    }

    fun bind(contact: Contact) {
        name.text = contact.name
        number.text = contact.number1
    }
}