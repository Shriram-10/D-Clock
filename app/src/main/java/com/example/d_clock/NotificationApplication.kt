package com.example.d_clock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

class NotificationApplication : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        val notificationChannel = NotificationChannel (
            "time_notification_channel",
            "Time",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.description = "A notification channel for displaying time"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}