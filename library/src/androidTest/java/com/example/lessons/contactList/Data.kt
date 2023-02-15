package com.example.lessons.contactList

import com.example.lessons.contacts.domain.entity.Contact
import java.util.GregorianCalendar

val danil = Contact(
    name = "Danil",
    number1 = "+79052550577",
    number2 = "911",
    email1 = "ddd@mail.ru",
    birthday = GregorianCalendar(2000, 9 , 6),
    id = "1"
)

val dima = Contact(
    name = "Dima",
    number1 = "+79054324577",
    number2 = "943543",
    email1 = "dimadd@mail.ru",
    id = "10"
)

val artem = Contact(
    name = "Artem",
    number1 = "+790525505545",
    number2 = "312412",
    email1 = "artem@mail.ru",
    id = "11"
)


val a = Contact(
    name = "Vlad",
    number1 = "+790525505545",
    id = "11"
)

val b = Contact(
    name = "Gena",
    number1 = "+790525505545",
    id = "11"
)

val c = Contact(
    name = "Vova",
    number1 = "+790525505545",
    id = "11"
)

val e = Contact(
    name = "Lena",
    number1 = "+790525505545",
    id = "11"
)

val d = Contact(
    name = "Sasha",
    number1 = "+790525505545",
    id = "11"
)

val f = Contact(
    name = "Ilya",
    number1 = "+790525505545",
    id = "11"
)

val contactListTestAndroid = listOf(danil, dima, artem, a, b, c, e, d,f )
