package com.example.common.address.domain.entity

import com.example.db.entity.ContactMapDbEntity

internal fun ContactMap.toContactMapDbEntity(): ContactMapDbEntity = ContactMapDbEntity(
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    id = id
)