package com.example.lessons.contactslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.Contact
import com.example.lessons.R
import com.example.lessons.contactslist.ContactsListFragment
import com.example.lessons.contactslist.ContactsListViewHolder

class ContactsListAdapter(private val listFragment: ContactsListFragment) :
    ListAdapter<Contact, ContactsListViewHolder>(DiffCallback()) {

    fun setContactsList(contactsList: List<Contact>?) {
        submitList(contactsList)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Contact>() {

        override fun areItemsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact) =
            oldItem.name == newItem.name && oldItem.number1 == newItem.number1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsListViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.contact_item, parent, false)
        val contactsListViewHolder = ContactsListViewHolder(itemView)
        itemView.setOnClickListener {
            val adapterPosition = contactsListViewHolder.absoluteAdapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listFragment.changeFragment(currentList[adapterPosition].id)
            }
        }
        return contactsListViewHolder
    }

    override fun onBindViewHolder(holder: ContactsListViewHolder, position: Int) {
        holder.bind(contact = currentList[position])
    }

    override fun getItemCount() = currentList.size
}
