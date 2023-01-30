package com.example.lessons.contactMapPicker.presentation.recyclerView

import android.view.View
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.contactMapPicker.presentation.model.ContactMapPicker
import com.example.library.R

internal class ContactMapPickerViewHolder(
    itemView: View, chooseElement: (String, Boolean) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val name: TextView = itemView.findViewById(R.id.nameMapTextView)
    private val address: TextView = itemView.findViewById(R.id.addressMapTextView)
    private var contactMapId: String? = null
    private var contactMapIsSelected: Boolean = false

    init {
        itemView.setOnClickListener {
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                contactMapId?.also { id -> chooseElement(id, !contactMapIsSelected) }
            }
        }
    }

    fun bind(contactMapPicker: ContactMapPicker) {
        name.text = contactMapPicker.name
        address.text = contactMapPicker.address
        contactMapId = contactMapPicker.id
        contactMapIsSelected = contactMapPicker.isSelected
        itemView.background = getDrawable(
            itemView.context,
            if (contactMapIsSelected) R.color.gray_light else return
        )
    }
}
