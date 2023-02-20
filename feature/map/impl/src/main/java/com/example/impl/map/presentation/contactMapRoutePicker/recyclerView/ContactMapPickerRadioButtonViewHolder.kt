package com.example.impl.map.presentation.contactMapRoutePicker.recyclerView

import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.impl.map.R
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.BUS_BUNDLE_PAIR
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.CAR_BUNDLE_PAIR
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.FOOT_BUNDLE_PAIR
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.MIXED_FORMAT_BUNDLE_PAIR
import com.example.impl.map.presentation.contactMap.ContactMapFragment.Companion.UNDERGROUND_BUNDLE_PAIR

internal class ContactMapPickerRadioButtonViewHolder(
    itemView: View,
    sendData: () -> Unit,
    checkRoute: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    init {
        itemView.findViewById<Button>(R.id.plotRouteButton).setOnClickListener {
            sendData()
        }

        itemView.findViewById<RadioGroup>(R.id.radioGroupMapPicker)
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonBus -> checkRoute(BUS_BUNDLE_PAIR)
                    R.id.radioButtonCar -> checkRoute(CAR_BUNDLE_PAIR)
                    R.id.radioButtonFoot -> checkRoute(FOOT_BUNDLE_PAIR)
                    R.id.radioButtonUnderground -> checkRoute(UNDERGROUND_BUNDLE_PAIR)
                    R.id.radioButtonMixedFormat -> checkRoute(MIXED_FORMAT_BUNDLE_PAIR)
                }
            }
    }
}
