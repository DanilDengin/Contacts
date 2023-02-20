package com.example.entity

import java.util.GregorianCalendar

data class Contact(
    val name: String,
    val numberPrimary: String,
    val numberSecondary: String? = null,
    val emailPrimary: String? = null,
    val emailSecondary: String? = null,
    val description: String? = null,
    val birthday: GregorianCalendar? = null,
    val id: String
)



