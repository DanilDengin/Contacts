package com.example.contact.impl.domain

import com.example.common.address.domain.entity.ContactMap
import com.example.contact.impl.data.model.ContactPhoneDb
import com.example.contact.impl.domain.entity.ContactDetails
import com.example.contact.impl.domain.entity.ContactList
import java.util.Calendar
import java.util.GregorianCalendar


val danilPhone = ContactPhoneDb(
    name = "Danil",
    numberPrimary = "+79052550577",
    numberSecondary = "911",
    emailPrimary = "ddd@mail.ru",
    birthday = GregorianCalendar(2000, 8 , 6),
    id = "1"
)

val dimaPhone = ContactPhoneDb(
    name = "Dima",
    numberPrimary = "+79054324577",
    numberSecondary = "943543",
    emailPrimary = "dimadd@mail.ru",
    id = "10"
)

val artemPhone = ContactPhoneDb(
    name = "Artem",
    numberPrimary = "+790525505545",
    numberSecondary = "312412",
    emailPrimary = "artem@mail.ru",
    id = "11"
)

val danilDetails = ContactDetails(
    name = "Danil",
    numberPrimary = "+79052550577",
    numberSecondary = "911",
    emailPrimary = "ddd@mail.ru",
    address = "Санкт-Петербург",
    birthday = "06.09.2000",
    id = "1"
)

val dimaDetails = ContactPhoneDb(
    name = "Dima",
    numberPrimary = "+79054324577",
    numberSecondary = "943543",
    emailPrimary = "dimadd@mail.ru",
    id = "10"
)

val artemDetails = ContactPhoneDb(
    name = "Artem",
    numberPrimary = "+790525505545",
    numberSecondary = "312412",
    emailPrimary = "artem@mail.ru",
    id = "11"
)

val danilList = ContactList(
    name = "Danil",
    numberPrimary = "+79052550577",
    id = "1"
)

val dimaList = ContactList(
    name = "Dima",
    numberPrimary = "+79054324577",
    id = "10"
)

val artemList = ContactList(
    name = "Artem",
    numberPrimary = "+790525505545",
    id = "11"
)

val danilContactMap = ContactMap(
    name = "Danil",
    address = "Санкт-Петербург",
    id = "1",
    latitude = 333.3,
    longitude = 333.3
)


val a = ContactPhoneDb(
    name = "Vlad",
    numberPrimary = "+790525505545",
    id = "11"
)

val b = ContactPhoneDb(
    name = "Gena",
    numberPrimary = "+790525505545",
    id = "11"
)

val c = ContactPhoneDb(
    name = "Vova",
    numberPrimary = "+790525505545",
    id = "11"
)

val e = ContactPhoneDb(
    name = "Lena",
    numberPrimary = "+790525505545",
    id = "11"
)

val d = ContactPhoneDb(
    name = "Sasha",
    numberPrimary = "+790525505545",
    id = "11"
)

val f = ContactPhoneDb(
    name = "Ilya",
    numberPrimary = "+790525505545",
    id = "11"
)

//val contactListTestAndroid = listOf(danilPhone, dima, artem, a, b, c, e, d, f )

val contactsPhoneTest = listOf(danilPhone, dimaPhone, artemPhone)

val contactsListTest = listOf(danilList, dimaList, artemList)

val contactSortedListTest = listOf(danilList, dimaList)

val currentDateTest= GregorianCalendar(1999, Calendar.SEPTEMBER, 9)
