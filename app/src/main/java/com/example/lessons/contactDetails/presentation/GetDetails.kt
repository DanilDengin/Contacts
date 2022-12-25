package com.example.lessons.contactDetails.presentation

import com.example.lessons.domain.contacts.entity.Contact

interface GetDetails {
    fun getDetails(contactForDetails: Contact)
}