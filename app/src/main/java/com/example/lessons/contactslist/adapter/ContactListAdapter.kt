package com.example.lessons.contactslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.lessons.Contact
import com.example.lessons.R
import com.example.lessons.contactslist.ContactListViewHolder

class ContactListAdapter(private val toContactDetailsById: (String) -> Unit) :
    ListAdapter<Contact, ContactListViewHolder>(DiffCallback()) {

    private class DiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.name == newItem.name && oldItem.number1 == newItem.number1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactListViewHolder(itemView) { adapterPosition ->
            toContactDetailsById(currentList[adapterPosition].id)
        }
    }

    override fun onBindViewHolder(holder: ContactListViewHolder, position: Int) {
        holder.bind(contact = currentList[position])
    }

    override fun getItemCount() = currentList.size
}
