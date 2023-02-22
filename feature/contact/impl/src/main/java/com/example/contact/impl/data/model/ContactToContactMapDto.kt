package com.example.contact.impl.data.model

import com.example.api.map.entity.ContactMapArguments
import com.example.contact.api.entity.Contact

internal fun Contact.toArguments(): ContactMapArguments = ContactMapArguments(
    name = name,
    id = id
)
