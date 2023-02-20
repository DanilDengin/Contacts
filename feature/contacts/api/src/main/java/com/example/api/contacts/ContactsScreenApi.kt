package com.example.api.contacts

import com.github.terrakok.cicerone.androidx.FragmentScreen

interface ContactsScreenApi {
    fun getListScreen() : FragmentScreen
    fun getDetailsScreen(contactId: Int) : FragmentScreen
}