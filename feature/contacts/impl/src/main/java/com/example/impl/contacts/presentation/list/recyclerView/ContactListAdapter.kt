package com.example.impl.contacts.presentation.list.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.impl.contacts.R
import com.example.entity.Contact

internal class ContactListAdapter(private val navigateToContactDetailsById: (String) -> Unit) :
    ListAdapter<com.example.entity.Contact, ContactListViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<com.example.entity.Contact>() {

        override fun areItemsTheSame(oldContact: com.example.entity.Contact, newContact: com.example.entity.Contact) =
            oldContact.id == newContact.id

        override fun areContentsTheSame(oldContact: com.example.entity.Contact, newContact: com.example.entity.Contact) =
            oldContact.name == newContact.name && oldContact.numberPrimary == newContact.numberPrimary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactListViewHolder(itemView, navigateToContactDetailsById)
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bind(contact = currentList[position])
    }

    override fun getItemCount() = currentList.size
}
