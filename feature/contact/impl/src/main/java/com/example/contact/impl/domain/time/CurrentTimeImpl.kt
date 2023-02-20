package com.example.contact.impl.domain.time

import javax.inject.Inject

class CurrentTimeImpl @Inject constructor() : CurrentTime {

    override fun getCurrentTime() = System.currentTimeMillis()
}