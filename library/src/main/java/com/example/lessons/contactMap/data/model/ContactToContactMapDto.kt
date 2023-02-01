package com.example.lessons.contactMap.data.model

import com.example.lessons.contacts.domain.entity.Contact

internal fun Contact.toArguments(): ContactMapArguments = ContactMapArguments(
    name = name,
    id = id
)
