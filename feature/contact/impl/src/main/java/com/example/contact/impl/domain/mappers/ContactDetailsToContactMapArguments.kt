package com.example.contact.impl.domain.mappers

import com.example.api.map.entity.ContactMapArguments
import com.example.contact.impl.domain.entity.ContactDetails

internal fun ContactDetails.toArguments(): ContactMapArguments = ContactMapArguments(
    name = name,
    id = id
)
