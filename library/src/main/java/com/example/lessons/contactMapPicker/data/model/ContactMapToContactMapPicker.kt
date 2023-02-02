package com.example.lessons.contactMapPicker.data.model

import com.example.lessons.contacts.domain.entity.ContactMap

internal fun ContactMap.toContactMapPicker(): ContactMapPicker = ContactMapPicker(
    name = name,
    address = address,
    id = id,
    isSelected = false
)