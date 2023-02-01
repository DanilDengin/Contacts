package com.example.lessons.contactMapPicker.presentation.model

internal data class ContactMapPicker(
    val name: String,
    val address: String,
    val id: String,
    var isSelected: Boolean
)