package com.example.impl.map.presentation.contactMapRoutePicker.recyclerView

import com.example.impl.map.R as FeatureRes
import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.impl.map.domain.entity.ContactMapPicker
import com.example.ui.R

internal class ContactMapPickerAddressViewHolder(
    itemView: View, chooseContact: (ContactMapPicker, Boolean) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(FeatureRes.id.nameMapTextView)
    private val address: TextView = itemView.findViewById(FeatureRes.id.addressMapTextView)
    private var contactMap: ContactMapPicker? = null

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                contactMap?.also { contact -> chooseContact(contact, !contact.isSelected) }
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
