package com.example.lessons.contactDetails.presentation

import com.example.lessons.contacts.domain.entity.Contact

interface GetDetails {
    fun getDetails(contactForDetails: Contact)
}