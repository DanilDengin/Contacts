package com.example.contact.impl.domain.time

import javax.inject.Inject

internal class CurrentTimeImpl @Inject constructor() : CurrentTime {

    override fun getCurrentTime() = System.currentTimeMillis()
}
