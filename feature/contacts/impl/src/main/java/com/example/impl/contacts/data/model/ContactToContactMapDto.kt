package com.example.impl.contacts.data.model

import com.example.entity.Contact
import com.example.entity.ContactMapArguments

internal fun Contact.toArguments(): ContactMapArguments = ContactMapArguments(
    name = name,
    id = id
)
