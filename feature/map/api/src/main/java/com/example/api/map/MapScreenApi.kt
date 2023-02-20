package com.example.api.map

import com.example.entity.ContactMapArguments
import com.github.terrakok.cicerone.androidx.FragmentScreen

interface MapScreenApi {
    fun getMapScreen(contactMapArgument: ContactMapArguments?) : FragmentScreen
}