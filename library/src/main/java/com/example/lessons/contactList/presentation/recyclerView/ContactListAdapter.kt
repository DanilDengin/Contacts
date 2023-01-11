package com.example.lessons.contactList.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.lessons.contacts.domain.entity.Contact
import com.example.library.R


internal class ContactListAdapter(private val navigateToContactDetailsById: (String) -> Unit) :
    ListAdapter<Contact, ContactListViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldContact: Contact, newContact: Contact) =
            oldContact.id == newContact.id

        override fun areContentsTheSame(oldContact: Contact, newContact: Contact) =
            oldContact == newContact
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactListViewHolder(itemView, navigateToContactDetailsById )
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bind(contact = currentList[position])
    }

    override fun getItemCount() = currentList.size
}