package com.example.impl.map

import com.example.api.map.MapScreenApi
import com.example.entity.ContactMapArguments
import com.example.impl.map.presentation.contactMap.ContactMapFragment
import com.example.impl.map.presentation.contactMapRoutePicker.ContactMapRoutePickerFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

class MapScreenApiImpl @Inject constructor() : MapScreenApi {
    override fun getMapScreen(contactMapArgument: ContactMapArguments?) =
        FragmentScreen { ContactMapFragment.newInstance(contactMapArgument) }
}