package com.example.lessons.contactMapPicker.presentation

sealed class ContactMapException{

    object ServerException : ContactMapException()

    object NetworkException : ContactMapException()

    object FatalException : ContactMapException()
}
