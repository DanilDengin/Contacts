package com.example.impl.map.data.address.local.room.model

import com.example.db.entity.ContactMapDbEntity
import com.example.impl.map.domain.entity.ContactMap

internal fun ContactMapDbEntity.toContactMap(): ContactMap = ContactMap(
    name = name,
    address = address,
    latitude = latitude,
    longitude = longitude,
    id = id
)