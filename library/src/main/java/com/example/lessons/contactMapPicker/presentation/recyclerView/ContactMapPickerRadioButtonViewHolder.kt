package com.example.lessons.contactMapPicker.presentation.recyclerView

import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lessons.contactMapPicker.presentation.PlotRouteButtonState
import com.example.lessons.utils.constans.BUS_BUNDLE_PAIR
import com.example.lessons.utils.constans.CAR_BUNDLE_PAIR
import com.example.lessons.utils.constans.FOOT_BUNDLE_PAIR
import com.example.lessons.utils.constans.MIXED_FORMAT_BUNDLE_PAIR
import com.example.lessons.utils.constans.UNDERGROUND_BUNDLE_PAIR
import com.example.library.R

class ContactMapPickerRadioButtonViewHolder(
    itemView: View,
    sendData: (Unit) -> Unit,
    initRadioGroupListener: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {


    init {
        itemView.findViewById<Button>(R.id.plotRouteButton).setOnClickListener {
            sendData(Unit)
        }

        itemView.findViewById<RadioGroup>(R.id.radioGroupMapPicker)
            .setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.radioButtonBus -> initRadioGroupListener(BUS_BUNDLE_PAIR)
                    R.id.radioButtonCar -> initRadioGroupListener(CAR_BUNDLE_PAIR)
                    R.id.radioButtonFoot -> initRadioGroupListener(FOOT_BUNDLE_PAIR)
                    R.id.radioButtonUnderground -> initRadioGroupListener(UNDERGROUND_BUNDLE_PAIR)
                    R.id.radioButtonMixedFormat -> initRadioGroupListener(MIXED_FORMAT_BUNDLE_PAIR)
                }
            }
    }

    fun bind(state: PlotRouteButtonState) {
        when(state){
            PlotRouteButtonState.VALID_DATA -> {
                itemView.findViewById<Button>(R.id.plotRouteButton).isEnabled = true
            }
            PlotRouteButtonState.NO_VALID_DATA -> {
                itemView.findViewById<Button>(R.id.plotRouteButton).isEnabled = true
            }
        }
    }
}
