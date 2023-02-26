package com.example.contact.impl.presentation.list.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.contact.impl.R
import com.example.contact.impl.domain.entity.ContactList

internal class ContactListAdapter(private val navigateToContactDetailsById: (String) -> Unit) :
    ListAdapter<ContactList, ContactListViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<ContactList>() {

        override fun areItemsTheSame(oldContact: ContactList, newContact: ContactList) =
            oldContact.id == newContact.id

        override fun areContentsTheSame(oldContact: ContactList, newContact: ContactList) =
            oldContact == newContact
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
