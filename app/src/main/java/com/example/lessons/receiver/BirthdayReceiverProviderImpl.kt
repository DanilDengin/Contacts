package com.example.lessons.receiver

import com.example.impl.contacts.domain.receiver.BirthdayReceiverProvider
import javax.inject.Inject

class BirthdayReceiverProviderImpl @Inject constructor(): BirthdayReceiverProvider {
    override fun getReceiver() = BirthdayReceiver()
}