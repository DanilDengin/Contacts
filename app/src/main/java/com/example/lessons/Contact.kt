package com.example.lessons

import java.util.GregorianCalendar

class Contact(
    val name: String,
    val number1: String,
    val number2: String? = null,
    val email1: String? = null,
    val email2: String? = null,
    val description: String? = null,
    val birthday: GregorianCalendar? = null,
    val id: String
)



