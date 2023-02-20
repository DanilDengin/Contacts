package com.example.impl.map.data.model

import com.example.impl.map.domain.entity.ContactMap

internal fun ContactMap.toContactMapPicker(): ContactMapPicker = ContactMapPicker(
    name = name,
    address = address,
    id = id,
    isSelected = false
)