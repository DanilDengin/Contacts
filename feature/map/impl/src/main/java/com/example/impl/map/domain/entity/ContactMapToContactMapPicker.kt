package com.example.impl.map.domain.entity

import com.example.common.address.domain.entity.ContactMap

internal fun ContactMap.toContactMapPicker(): ContactMapPicker = ContactMapPicker(
    name = name,
    address = address,
    id = id,
    isSelected = false
)