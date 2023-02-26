package com.example.contact.impl.data.model

import java.util.GregorianCalendar

data class ContactPhoneDb(
    val name: String,
    val numberPrimary: String,
    val numberSecondary: String? = null,
    val emailPrimary: String? = null,
    val emailSecondary: String? = null,
    val birthday: GregorianCalendar? = null,
    val id: String
)