package com.example.impl.map

import com.example.api.map.entity.ContactMapArguments
import com.example.api.map.screen.MapScreenApi
import com.example.impl.map.presentation.contactMap.ContactMapFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

class MapScreenApiImpl @Inject constructor() : MapScreenApi {
    override fun getMapScreen(contactMapArgument: ContactMapArguments?) =
        FragmentScreen { ContactMapFragment.newInstance(contactMapArgument) }
}