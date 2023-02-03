package com.example.lessons.contactDetails.presentation

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.lessons.presentation.MainActivity
import com.example.library.R
import java.util.Calendar

internal class BirthdayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val contactId =
            intent.getIntExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, BIRTHDAY_CONTACT_DEFAULT_ID)
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, contactId)
        val notificationPendingIntent = PendingIntent.getActivity(
            context,
            contactId,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.birthday_notification_title))
            .setContentText(intent.getStringExtra(BIRTHDAY_CONTACT_NAME_INTENT_KEY))
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        (context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager)
            .notify(contactId, builder.build())

        intent.getStringExtra(BIRTHDAY_CONTACT_NAME_INTENT_KEY)?.let { contactName ->
            repeatAlarm(context, contactId, contactName)
        }
    }

    private fun repeatAlarm(context: Context, contactId: Int, contactName: String) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.add(Calendar.YEAR, 1)
        val intentBirthdayReceiver = Intent(BIRTHDAY_RECEIVER_INTENT_ACTION)
        intentBirthdayReceiver
            .setClass(context, BirthdayReceiver::class.java)
            .putExtra(BIRTHDAY_CONTACT_NAME_INTENT_KEY, contactName)
            .putExtra(BIRTHDAY_CONTACT_ID_INTENT_KEY, contactId)
        val pendingIntentBirthday = PendingIntent.getBroadcast(
            context,
            contactId,
            intentBirthdayReceiver,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).set(
            AlarmManager.RTC,
            calendar.timeInMillis,
            pendingIntentBirthday
        )
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "birthday"
        const val BIRTHDAY_RECEIVER_INTENT_ACTION = "birthdayReceiver"
        const val BIRTHDAY_CONTACT_NAME_INTENT_KEY = "contactName"
        const val BIRTHDAY_CONTACT_ID_INTENT_KEY = "contactId"
        const val BIRTHDAY_CONTACT_DEFAULT_ID = -1
    }
}

