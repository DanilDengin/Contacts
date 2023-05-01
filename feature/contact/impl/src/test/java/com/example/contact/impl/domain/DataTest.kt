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
    birthday = GregorianCalendar(2000, Calendar.SEPTEMBER, 6),
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

val contactsPhoneTest = listOf(danilPhone, dimaPhone, artemPhone)

val contactsListTest = listOf(danilList, dimaList, artemList)

val contactSortedListTest = listOf(danilList, dimaList)

val currentDateTest = GregorianCalendar(2023, Calendar.OCTOBER, 9)
