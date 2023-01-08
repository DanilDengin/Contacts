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


class BirthdayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.putExtra("contactId", intent.getIntExtra("contactId", -1))
        val notificationPendingIntent = PendingIntent.getActivity(
            context, intent.getIntExtra("contactId", -1),
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "Birthday")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(intent.getStringExtra("nameOfContact"))
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        val notification = builder.build()
        val notificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(intent.getIntExtra("contactId", -1), notification)

        intent.getStringExtra("nameOfContact")
            ?.let { repeatAlarm(context, intent.getIntExtra("contactId", -1), it) }
    }

    private fun repeatAlarm(context: Context, id: Int, nameContact: String) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.add(Calendar.YEAR, 1)
        val intentBirthdayReceiver = Intent("birthdayReceiver")
        intentBirthdayReceiver
            .setClass(context, BirthdayReceiver::class.java)
            .putExtra("nameOfContact", nameContact)
            .putExtra("contactId", id)
        val pendingIntentBirthday = PendingIntent.getBroadcast(
            context,
            id,
            intentBirthdayReceiver,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC, calendar.timeInMillis, pendingIntentBirthday)
    }

}

