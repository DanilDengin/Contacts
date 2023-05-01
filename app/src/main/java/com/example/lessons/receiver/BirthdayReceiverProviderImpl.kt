package com.example.lessons.receiver

import com.example.contact.impl.domain.receiver.BirthdayReceiverProvider
import javax.inject.Inject

internal class BirthdayReceiverProviderImpl @Inject constructor() : BirthdayReceiverProvider {
    override fun getReceiver() = BirthdayReceiver()
}
