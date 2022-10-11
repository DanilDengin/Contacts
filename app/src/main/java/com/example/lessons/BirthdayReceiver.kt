package com.example.lessons

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService


class BirthdayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val resultIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        resultIntent.putExtra("contactId", intent.getIntExtra("contactId", -1))
        val resultPendingIntent = PendingIntent.getActivity(
            context, intent.getIntExtra("contactId", -1),
            resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        var builder = NotificationCompat.Builder(context, "Birthday")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Don't forget to congratulate")
            .setContentText(intent.getStringExtra("nameOfContact"))
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        val notification = builder.build()
        val notificationManager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(intent.getIntExtra("contactId", -1), notification)
    }


}

