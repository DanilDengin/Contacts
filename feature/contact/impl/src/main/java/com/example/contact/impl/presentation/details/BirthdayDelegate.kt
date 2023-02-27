package com.example.contact.impl.presentation.details

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.contact.impl.domain.receiver.BirthdayReceiverProvider
import com.example.ui.R
import com.example.utils.constans.BIRTHDAY_CONTACT_ID_INTENT_KEY
import com.example.utils.constans.BIRTHDAY_CONTACT_NAME_INTENT_KEY
import com.example.utils.constans.BIRTHDAY_RECEIVER_INTENT_ACTION
import com.example.utils.delegate.unsafeLazy
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class BirthdayDelegate @AssistedInject constructor(
    @Assisted private val contactId: Int,
    private val context: Context,
    private val birthdayReceiverProvider: BirthdayReceiverProvider
    ) {

    private val intentBirthday: Intent by unsafeLazy {
        Intent(BIRTHDAY_RECEIVER_INTENT_ACTION)
    }

    private val pendingIntentBirthday: PendingIntent by unsafeLazy {
        PendingIntent.getBroadcast(
            context,
            contactId,
            intentBirthday,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private val alarmBirthday: AlarmManager by unsafeLazy {
        context.getSystemService(
            AppCompatActivity.ALARM_SERVICE
        ) as AlarmManager
    }

    private val birthdayNotificationText: String by unsafeLazy {
        context.resources.getString(R.string.birthday_notification_text)
    }

    fun checkBirthdaySwitchState(contactName: String): Boolean {
        intentBirthday.setClass(
            context,
            birthdayReceiverProvider.getReceiver()::class.java
        )

        intentBirthday
            .putExtra(
                BIRTHDAY_CONTACT_NAME_INTENT_KEY,
                String.format(birthdayNotificationText, " $contactName")
            )
            .putExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, contactId)

        return PendingIntent.getBroadcast(
            context,
            contactId,
            intentBirthday,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        ) != null
    }

    fun doAlarm(alarmDate: Long) {
        alarmBirthday.set(
            AlarmManager.RTC,
            alarmDate,
            pendingIntentBirthday
        )
    }

    fun cancelAlarm() {
        pendingIntentBirthday.cancel()
        alarmBirthday.cancel(pendingIntentBirthday)
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted contactId: Int): BirthdayDelegate
    }
}