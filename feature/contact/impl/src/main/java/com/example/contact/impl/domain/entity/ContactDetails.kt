package com.example.contact.impl.domain.entity

data class ContactDetails(
    val name: String,
    val numberPrimary: String,
    val numberSecondary: String? = null,
    val emailPrimary: String? = null,
    val emailSecondary: String? = null,
    val address: String? = null,
    val birthday: String? = null,
    val id: String
)



