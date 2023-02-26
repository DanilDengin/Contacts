package com.example.contact.impl.domain

import com.example.contact.impl.domain.entity.ContactDetails
import java.util.Calendar
import java.util.GregorianCalendar


val danil = ContactDetails(
    name = "Danil",
    numberPrimary = "+79052550577",
    numberSecondary = "911",
    emailPrimary = "ddd@mail.ru",
    birthday = GregorianCalendar(2000, 9 , 6),
    id = "1"
)

val dima = ContactDetails(
    name = "Dima",
    numberPrimary = "+79054324577",
    numberSecondary = "943543",
    emailPrimary = "dimadd@mail.ru",
    id = "10"
)

val artem = ContactDetails(
    name = "Artem",
    numberPrimary = "+790525505545",
    numberSecondary = "312412",
    emailPrimary = "artem@mail.ru",
    id = "11"
)


val a = ContactDetails(
    name = "Vlad",
    numberPrimary = "+790525505545",
    id = "11"
)

val b = ContactDetails(
    name = "Gena",
    numberPrimary = "+790525505545",
    id = "11"
)

val c = ContactDetails(
    name = "Vova",
    numberPrimary = "+790525505545",
    id = "11"
)

val e = ContactDetails(
    name = "Lena",
    numberPrimary = "+790525505545",
    id = "11"
)

val d = ContactDetails(
    name = "Sasha",
    numberPrimary = "+790525505545",
    id = "11"
)

val f = ContactDetails(
    name = "Ilya",
    numberPrimary = "+790525505545",
    id = "11"
)

val contactListTestAndroid = listOf(danil, dima, artem, a, b, c, e, d, f )

val contactListTest = listOf(danil, dima, artem)

val contactSortedListTest = listOf(danil, dima)

val currentDateTest= GregorianCalendar(1999, Calendar.SEPTEMBER, 9)
