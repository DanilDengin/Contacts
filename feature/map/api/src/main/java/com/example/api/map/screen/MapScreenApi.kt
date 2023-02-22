package com.example.api.map.screen

import com.example.api.map.entity.ContactMapArguments
import com.github.terrakok.cicerone.androidx.FragmentScreen

interface MapScreenApi {
    fun getMapScreen(contactMapArgument: ContactMapArguments?): FragmentScreen
}