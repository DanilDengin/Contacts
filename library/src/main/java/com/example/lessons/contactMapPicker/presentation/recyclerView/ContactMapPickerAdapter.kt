package com.example.lessons.contactMapPicker.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.contactMapPicker.presentation.model.ContactMapPicker
import com.example.library.R

internal class ContactMapPickerAdapter(
    private val contactMapPickerList: List<ContactMapPicker>,
    private val chooseElement: (String, Boolean) -> Unit
) : RecyclerView.Adapter<ContactMapPickerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactMapPickerViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_map_item, parent, false)
        return ContactMapPickerViewHolder(itemView, chooseElement)
    }

    override fun onBindViewHolder(holder: ContactMapPickerViewHolder, position: Int) {
        holder.bind(contactMapPicker = contactMapPickerList[position])
    }

    override fun getItemCount(): Int = contactMapPickerList.size

}