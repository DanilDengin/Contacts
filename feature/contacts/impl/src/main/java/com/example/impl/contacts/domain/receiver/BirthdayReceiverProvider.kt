package com.example.impl.contacts.domain.receiver

import android.content.BroadcastReceiver

interface BirthdayReceiverProvider {
    fun getReceiver() : BroadcastReceiver
}