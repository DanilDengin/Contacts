package com.example.contact.impl.presentation.list.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.contact.impl.R
import com.example.contact.impl.domain.entity.ContactList

internal class ContactListViewHolder(
    itemView: View,
    navigateToContactDetailsById: (String) -> Unit
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

    fun bind(contact: ContactList) {
        name.text = contact.name
        number.text = contact.numberPrimary
        contactId = contact.id
    }
}
