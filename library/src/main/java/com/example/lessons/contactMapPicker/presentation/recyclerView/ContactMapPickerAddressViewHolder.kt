package com.example.lessons.contactMapPicker.presentation.recyclerView

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.contactMapPicker.data.model.ContactMapPicker
import com.example.library.R

internal class ContactMapPickerAddressViewHolder(
    itemView: View, chooseElement: (ContactMapPicker, Boolean) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.nameMapTextView)
    private val address: TextView = itemView.findViewById(R.id.addressMapTextView)
    private var contactMap: ContactMapPicker? = null

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                contactMap?.also { contact -> chooseElement(contact, !contact.isSelected) }
            }
        }
    }

    fun bind(contactMapPicker: ContactMapPicker) {
        name.text = contactMapPicker.name
        address.text = contactMapPicker.address
        contactMap = contactMapPicker
        itemView.background = getDrawable(
            itemView.context,
            if (contactMapPicker.isSelected) R.color.gray_light else R.color.transparent
        )
    }
}
