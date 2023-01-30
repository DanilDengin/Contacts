package com.example.lessons.contactMap.data.address.local.room.model

import com.example.lessons.contacts.domain.entity.ContactMap

internal fun ContactMapDbEntity.toContactMap(): ContactMap = ContactMap(
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    id = id
)