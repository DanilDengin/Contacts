package com.example.lessons.contacts.domain.utils.time

import javax.inject.Inject

class CurrentTimeImpl @Inject constructor(): CurrentTime {

    override fun getCurrentTime() = System.currentTimeMillis()
}