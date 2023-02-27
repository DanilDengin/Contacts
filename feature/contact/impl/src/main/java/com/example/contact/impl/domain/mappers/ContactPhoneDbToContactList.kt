package com.example.contact.impl.domain.mappers

import com.example.contact.impl.data.model.ContactPhoneDb
import com.example.contact.impl.domain.entity.ContactList

fun ContactPhoneDb.toContactList() = ContactList(
    name = name,
    numberPrimary = numberPrimary,
    id = id
)