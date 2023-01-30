package com.example.lessons.contactMap.data.address.local.room.model

import com.example.lessons.contacts.domain.entity.ContactMap

internal fun ContactMap.toContactMapDbEntity(): ContactMapDbEntity = ContactMapDbEntity(
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    id = id
)