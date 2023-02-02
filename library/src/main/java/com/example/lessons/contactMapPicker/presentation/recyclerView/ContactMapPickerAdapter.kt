package com.example.lessons.contactMapPicker.presentation.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.contactMapPicker.data.model.ContactMapPicker
import com.example.lessons.contactMapPicker.presentation.PlotRouteButtonState
import com.example.library.R

internal class ContactMapPickerAdapter(
    private val chooseElement: (ContactMapPicker, Boolean) -> Unit,
    private val sendData: (Unit) -> Unit,
    private val initRadioGroupListener: (String) -> Unit
) : ListAdapter<ContactMapPicker, RecyclerView.ViewHolder>(DiffCallback()) {

    var state =PlotRouteButtonState.NO_VALID_DATA

    private class DiffCallback : DiffUtil.ItemCallback<ContactMapPicker>() {

        override fun areItemsTheSame(oldContact: ContactMapPicker, newContact: ContactMapPicker) =
            oldContact.id == newContact.id

        override fun areContentsTheSame(
            oldContact: ContactMapPicker,
            newContact: ContactMapPicker
        ) =
            oldContact.isSelected == newContact.isSelected
    }

    override fun getItemViewType(position: Int)
        = if (position ==  currentList.size -1 ) ENDING_VIEW_TYPE else CONTACT_MAP_VIEW_TYPE


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == CONTACT_MAP_VIEW_TYPE) {
            val view = layoutInflater.inflate(R.layout.contact_map_item, parent, false)
            ContactMapPickerAddressViewHolder(view, chooseElement)
        } else {
            val view = layoutInflater.inflate(R.layout.contact_map_radio_button_item, parent, false)
            ContactMapPickerRadioButtonViewHolder(view, sendData, initRadioGroupListener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ContactMapPickerAddressViewHolder -> holder.bind(contactMapPicker = currentList[position])
            is ContactMapPickerRadioButtonViewHolder -> holder.bind(state = state)
        }
    }

    override fun getItemCount(): Int = currentList.size

    companion object {
        const val CONTACT_MAP_VIEW_TYPE = 0
        const val ENDING_VIEW_TYPE = 1
    }
}