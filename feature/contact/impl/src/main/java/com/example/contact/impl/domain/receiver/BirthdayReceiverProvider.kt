package com.example.contact.impl.domain.receiver

import android.content.BroadcastReceiver

interface BirthdayReceiverProvider {
    fun getReceiver(): BroadcastReceiver
}