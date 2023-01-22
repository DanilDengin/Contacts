package com.example.lessons.contactList.presentation.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.contacts.domain.entity.Contact
import com.example.library.R

internal class ContactListViewHolder(
    itemView: View, navigateToContactDetailsById: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.nameListTextView)
    private val number: TextView = itemView.findViewById(R.id.numberListTextView)
    private var contactId: String? = null

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                contactId?.also(navigateToContactDetailsById)
            }
        }
    }

    fun bind(contact: Contact) {
        name.text = contact.name
        number.text = contact.number1
        contactId = contact.id
    }
}