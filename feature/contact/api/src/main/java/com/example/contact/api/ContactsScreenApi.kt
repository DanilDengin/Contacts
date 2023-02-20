package com.example.contact.api

import com.github.terrakok.cicerone.androidx.FragmentScreen

interface ContactsScreenApi {
    fun getListScreen(): FragmentScreen
    fun getDetailsScreen(contactId: Int): FragmentScreen
}