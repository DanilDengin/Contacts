package com.example.common.address.domain.entity

import com.example.db.entity.ContactMapDbEntity

internal fun ContactMapDbEntity.toContactMap(): ContactMap = ContactMap(
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    id = id
)
